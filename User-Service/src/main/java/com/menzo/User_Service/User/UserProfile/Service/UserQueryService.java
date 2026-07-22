package com.menzo.User_Service.User.UserProfile.Service;

import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.UserProfile.Dto.*;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Enum.ActiveStatus;
import com.menzo.User_Service.User.UserProfile.Enum.UserTypes;
import com.menzo.User_Service.User.UserProfile.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserQueryService {

    private static final Logger logger = LoggerFactory.getLogger(UserQueryService.class);

    @Autowired
    private UserRepository userRepo;


    /*
     *
     *   Get user details
     *   User identified by user email
     *
     */
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


    /*
     *
     *   Get user status
     *   User identified by user ID
     *
     */
    public UserStatusDto getUserStatusByUserId(Long userId) {
        try {
            logger.info("Fetching user by ID: {}", userId);
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
            return new UserStatusDto(user);
        } catch (Exception e) {
            logger.error("Error fetching user by ID: {}", userId);
            throw e;
        }
    }


    /*
     *
     *   get user details
     *   User identified by user email
     *   for client side
     *
     */
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


    /*
     *
     *   Get users list with pagination
     *
     */
    public Page<UserListingDto> getUsersListWithPagination(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(Sort.Direction.DESC, "id"));
            Page<User> users = userRepo.findAll(pageable);

            List<UserListingDto> usersDto = new ArrayList<>();

            for (User user : users.getContent()) {
                if (user.getUserType() == UserTypes.ADMIN) continue;
                ActiveStatus activeStatus = user.isActive() ? ActiveStatus.ACTIVE : ActiveStatus.INACTIVE;
                usersDto.add(new UserListingDto(
                        user.getUserId(),
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


    /*
     *
     *   Get user details
     *   User identified by user ID
     *   for Admin side
     *
     */
    public UserDetailsDto getUserDetailsByIdForAdminSide(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        logger.info("User found for ID: {}", user.getUserId());

        if (user.getUserType().equals(UserTypes.ADMIN)) {
            logger.warn("No user with 'USER' role found for ID: {}", user.getUserId());
            return null;
        }
        logger.info("Making UserDetailsDto for user ID: {}", userId);
        return UserDetailsDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .createdAt(user.getCreatedAt())
                .isActive(user.isActive())
                .build();
    }


    /*
     *
     *   Get user details
     *   User identified by user email
     *   Used for UserCommandController
     *
     */
    public UserDetailsDto getUserDetailsByEmail(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("No user found with Email: " + userEmail));
        return UserDetailsDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .createdAt(user.getCreatedAt())
                .passwordPresent(user.getPassword() != null ? true : false)
                .isActive(user.isActive())
                .build();
    }


    /*
     *
     *   User email existence check
     *
     */
    public boolean isUserEmailExists(EmailDto emailDto) {
        return userRepo.existsByEmail(emailDto.getEmail());
    }
}
