package com.menzo.User_Service.Service;

import com.menzo.User_Service.Dto.*;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Entity.UserAddress;
import com.menzo.User_Service.Enums.ActiveStatus;
import com.menzo.User_Service.Enums.Gender;
import com.menzo.User_Service.Enums.Roles;
import com.menzo.User_Service.Repository.AddressRepository;
import com.menzo.User_Service.Repository.UserAddressRepository;
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
import java.util.stream.Collectors;

@Service
public class UserRetrievalService {

    private static final Logger logger = LoggerFactory.getLogger(UserRetrievalService.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserAddressRepository userAddressRepo;

    @Autowired
    private AddressRepository addressRepo;

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

//    ********* User Address *********

    public List<UserAddressDto> getAllAddressByEmail(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        List<UserAddress> userAddresses = userAddressRepo.findByUser(user);
        if (userAddresses == null) {
            throw new EntityNotFoundException("User has no addresses");
        }
        List<UserAddressDto> userAddressDtos = userAddresses.stream()
                .map(userAddress -> {
                    return new UserAddressDto(
                            userAddress.getId(),
                            userAddress.getFirstName(),
                            userAddress.getLastName(),
                            userAddress.getPhoneNumber(),
                            userAddress.getAddress().getUnitAddress(),
                            userAddress.getAddress().getStreet(),
                            userAddress.getAddress().getLandmark(),
                            userAddress.getAddress().getCity(),
                            userAddress.getAddress().getState(),
                            userAddress.getAddress().getCountry().getCountryName(),
                            userAddress.getAddress().getPincode(),
                            userAddress.getIsDefault()
                    );
                }).collect(Collectors.toList());

        return userAddressDtos;
    }

    public UserAddressDto getDefaultAddressByEmail(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        List<UserAddress> userAddresses = userAddressRepo.findByUser(user);
        if (userAddresses == null) {
            throw new EntityNotFoundException("User has no addresses");
        }
        UserAddress defaultAddress = userAddresses.stream()
                .filter(userAddress -> userAddress.getIsDefault())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User doesn't have default address"));

        return new UserAddressDto(
                defaultAddress.getId(),
                defaultAddress.getFirstName(),
                defaultAddress.getLastName(),
                defaultAddress.getPhoneNumber(),
                defaultAddress.getAddress().getUnitAddress(),
                defaultAddress.getAddress().getStreet(),
                defaultAddress.getAddress().getLandmark(),
                defaultAddress.getAddress().getCity(),
                defaultAddress.getAddress().getState(),
                defaultAddress.getAddress().getCountry().getCountryName(),
                defaultAddress.getAddress().getPincode(),
                defaultAddress.getIsDefault()
        );
    }
}
