package com.menzo.User_Service.Service;

import com.menzo.User_Service.Dto.*;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Enums.ActiveStatus;
import com.menzo.User_Service.Enums.Roles;
import com.menzo.User_Service.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserRetrievalService {

    private static final Logger logger = LoggerFactory.getLogger(UserRetrievalService.class);

    @Autowired
    private UserRepository userRepo;

    public UserDto getUserbyEmail(EmailDto userEmail) {
        try {
            logger.info("Fetching user by email: {}", userEmail.getEmail());
            User user = userRepo.findByEmail(userEmail.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            return new UserDto(user);
        } catch (Exception e) {
            logger.error("Error fetching user by email: {}", e.getMessage(), e);
            throw e;
        }
    }

    public UserStatusDto getUserByUserId(Long userId) {
        try{
            logger.info("Fetching user by ID: {}", userId);
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
            return new UserStatusDto(user);
        } catch (Exception e) {
            logger.error("Error fetching user by ID: {}", userId);
            throw e;
        }
    }

    public ClientSideUserDetailsDto getUserDetailsForClientSide(EmailDto userEmail) {
        try {
            User user = userRepo.findByEmail(userEmail.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found."));
            return new ClientSideUserDetailsDto(user);
        } catch (Exception e) {
            logger.error("Error fetching user by email: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Page<UserListingDto> getUsersListWithPagination(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(Sort.Direction.DESC, "id"));
            Page<User> users = userRepo.findAll(pageable);

            List<UserListingDto> usersDto = new ArrayList<>();

            for (User user : users.getContent()) {
                if (user.getRoles() == Roles.ADMIN) continue;
                ActiveStatus activeStatus = user.isActive() ? ActiveStatus.ACTIVE : ActiveStatus.INACTIVE;
                usersDto.add(new UserListingDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        activeStatus
                ));
            }
            return new PageImpl<>(usersDto, pageable, users.getTotalElements());
        } catch (Exception e) {
            logger.error("Error getting users list with pagination", e);
            return null;
        }

    }

    public UserDetailsDto getUserDetailsByIdForAdminSide(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        logger.info("User found for ID: {}", user.getId());

        if (user.getRoles().equals(Roles.ADMIN)) {
            logger.warn("No user with 'USER' role found for ID: {}", user.getId());
            return null;
        }
        logger.info("Making UserDetailsDto for user ID: {}", userId);
        return new UserDetailsDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getCreatedAt(),
                user.isActive()
        );
    }

    public UserDetailsDto getUserDetailsByEmail(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("No user found with Email: " + userEmail));
        UserDetailsDto userDetails = new UserDetailsDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getCreatedAt(),
                user.isActive()
        );
        userDetails.setPasswordPresent(user.getPassword() != null ? true : false);

        return userDetails;
    }

}
