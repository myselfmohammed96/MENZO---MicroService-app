package com.menzo.User_Service.Service;

import com.menzo.User_Service.Dto.*;
import com.menzo.User_Service.Entity.Address;
import com.menzo.User_Service.Entity.Country;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Entity.UserAddress;
import com.menzo.User_Service.Enums.Roles;
import com.menzo.User_Service.Exceptions.AuthFeignException;
import com.menzo.User_Service.Exceptions.DuplicateAddressException;
import com.menzo.User_Service.Exceptions.PasswordMismatchException;
import com.menzo.User_Service.Feign.AuthFeign;
import com.menzo.User_Service.Repository.AddressRepository;
import com.menzo.User_Service.Repository.CountryRepository;
import com.menzo.User_Service.Repository.UserAddressRepository;
import com.menzo.User_Service.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jdk.jshell.spi.ExecutionControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private UserAddressRepository userAddressRepo;

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private AuthFeign authFeign;

    //  Register new user
    public Cookie registerNewUser(RegNewUser newUser) {
        UserDto savedUser = saveNewUser(newUser);
        TokenMinimalDto jwtToken = null;
        try {
            jwtToken = authFeign.getToken(new EmailDto(newUser.getEmail()));
        } catch (AuthFeignException ex) {
            logger.error("Feign error while JWT token: status = {}, message = {}", ex.getStatus(), ex.getMessage());
            throw new RuntimeException("Identity service failed while creating JWT token", ex);
        }
        if (jwtToken == null || jwtToken.getToken() == null) {
            logger.error("JWT token is null");
            throw new RuntimeException("JWT token is null");
        }
        return createCookie(jwtToken.getToken());
    }

    //    Saving new Registered user
    private UserDto saveNewUser(RegNewUser newUser) {
        try {
            logger.info("Registering new user with email: {}", newUser.getEmail());
            if (userRepo.existsByEmail(newUser.getEmail())) {
                throw new IllegalArgumentException("Email is already in use.");
            }

            String encodedPassword = encodePassword(newUser.getPassword());
            User user = new User(newUser.getFirstName(), newUser.getLastName(),
                    newUser.getPhoneNumber(), newUser.getEmail(),
                    newUser.getDateOfBirth(), newUser.getGender(), encodedPassword);
            user.setRoles(Roles.USER);
            user.setActive(true);

            User savedUser = userRepo.save(user);
            if (savedUser == null || savedUser.getId() == null) {
                throw new RuntimeException("Failed to save user");
            }
            logger.info("User saved successfully: {}", user.getEmail());
            return new UserDto(savedUser);
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String encodePassword(String userPassword) {
        try {
            logger.info("Encoding password");
            PasswordDto encodedPasswordDto = null;
            try {
                encodedPasswordDto = authFeign.encodePassword(new PasswordDto(userPassword));
            } catch (AuthFeignException ex) {
                logger.error("Feign error while encoding password: status = {}, message = {}", ex.getStatus(), ex.getMessage());
                throw new RuntimeException("Identity service failed to encode password", ex);
            }
            if (encodedPasswordDto == null || encodedPasswordDto.getPassword() == null) {
                logger.error("Encoded password is null");
                throw new RuntimeException("Encoded password is null");
            }
            return encodedPasswordDto.getPassword();
        } catch (Exception e) {
            logger.error("Password encoding failed: {}", e.getMessage(), e);
            throw new RuntimeException("Password encoding failed", e);
        }
    }

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(10 * 24 * 60 * 60);
        return cookie;
    }


    public User updateUserActiveStatus(UpdateUserActiveStatusDto statusDto) {
        try {
            User user = userRepo.findById(statusDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + statusDto.getUserId()));
            if (user.isActive() != statusDto.isBlock()) {
                throw new RuntimeException("Invalid Input, both user activeStatus & block request should be same.");
            }
            user.setActive(user.isActive() && statusDto.isBlock() ? false : true);
            return userRepo.save(user);
        } catch (Exception e) {
            logger.error("Error updating user active status: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating user activeStatus", e);
        }

    }

    public Long updateUserDetails(String userEmail, UserDto latestUser) {
        System.out.println(latestUser);
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Email: " + userEmail));

        User updatedUser = null;
        try {
            user.setFirstName(latestUser.getFirstName() != null && !latestUser.getFirstName().isEmpty() ? latestUser.getFirstName() : user.getFirstName());
            user.setLastName(latestUser.getLastName() != null && !latestUser.getLastName().isEmpty() ? latestUser.getLastName() : user.getLastName());

            user.setPhoneNumber(latestUser.getPhoneNumber() != null && !latestUser.getPhoneNumber().isEmpty() ? latestUser.getPhoneNumber() : user.getPhoneNumber());
            user.setDateOfBirth(latestUser.getDateOfBirth() != null ? latestUser.getDateOfBirth() : user.getDateOfBirth());
            user.setGender(latestUser.getGender() != null ? latestUser.getGender() : user.getGender());

            updatedUser = userRepo.save(user);
        } catch (RuntimeException e) {
            logger.error("Error while updating user with email: {}", userEmail);
            throw new RuntimeException("Error while updating User with email: " + userEmail);
        }
        if (Objects.equals(latestUser.getFirstName(), updatedUser.getFirstName()) ||
                Objects.equals(latestUser.getLastName(), updatedUser.getLastName()) ||
                Objects.equals(latestUser.getPhoneNumber(), updatedUser.getPhoneNumber()) ||
                Objects.equals(latestUser.getDateOfBirth(), updatedUser.getDateOfBirth()) ||
                Objects.equals(latestUser.getGender(), updatedUser.getGender())) {
            return updatedUser.getId();
        } else {
            return null;
        }
    }

    public boolean updatePassword(String userEmail, boolean passwordPresent, ChangePasswordDto passwordDto) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Email: " + userEmail));
        try {
            User updatedUser = null;
            if (passwordPresent) {
                logger.info("Verifying current password to user: {}", userEmail);
                Boolean passwordMatches = authFeign.verifyPassword(new VerifyPasswordDto(passwordDto.getCurrentPassword(), user.getPassword()));
                if (passwordMatches) {
                    logger.info("Current password verification successful.");
                    updatedUser = changePassword(user, passwordDto.getNewPassword());
                } else {
                    logger.error("Current password mismatched.");
                    throw new PasswordMismatchException("Current password didn't match.");
                }
            } else {
                logger.info("Adding new password to user: {}", userEmail);
                updatedUser = changePassword(user, passwordDto.getNewPassword());
            }
            return updatedUser != null;
        } catch (PasswordMismatchException e) {
            throw new PasswordMismatchException(e.getMessage());
        } catch (Exception e) {
            logger.error("Password update failed.");
            throw new RuntimeException("Password update failed", e);
        }
    }

    private User changePassword(User user, String newPassword) {
        try {
            String encodedPassword = encodePassword(newPassword);
            user.setPassword(encodedPassword);
            return userRepo.save(user);
        } catch (Exception e) {
            logger.error("Password changing failed.");
            throw new RuntimeException("Password changing failed.", e);
        }
    }

    public boolean isUserEmailExists(EmailDto emailDto) {
        boolean emailExists = userRepo.existsByEmail(emailDto.getEmail());
        return emailExists;
    }

//    ********* Google OAuth *********

    public UserDto saveGoogleOAuthUser(OAuthUserDto googleUser) {
        if (googleUser == null || googleUser.getEmail() == null) {
            logger.error("Google OAuth user or email is null");
            throw new IllegalArgumentException("Invalid Google OAuth user data");
        }
        try {
            return userRepo.findByEmail(googleUser.getEmail())
                    .map(user -> {
                        logger.info("User already exists with email: {}", googleUser.getEmail());
                        return new UserDto(user);
                    })
                    .orElseGet(() -> {
                        User newUser = new User(googleUser);
                        newUser.setRoles(Roles.USER);
                        newUser.setActive(true);
                        User savedUser = userRepo.save(newUser);
                        logger.info("New Google OAuth user saved: {}", savedUser.getEmail());
                        return new UserDto(savedUser);
                    });
        } catch (DataAccessException e) {
            logger.error("Database error while saving/fetching Google OAuth user: {}", googleUser.getEmail(), e);
            throw new RuntimeException("Database error while processing OAuth login", e);
        } catch (Exception e) {
            logger.error("Unexpected error during Google OAuth user save: {}", googleUser.getEmail(), e);
            throw new RuntimeException("Unexpected error during OAuth login", e);
        }
    }

//    ********* User Address *********

    public Long addUserAddress(String userEmail, UserAddressDto userAddress) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        Country country = countryRepo.findByCountryNameIgnoreCase(userAddress.getCountry())
                .orElseThrow(() -> new EntityNotFoundException("Country not found with name: " + userAddress.getCountry()));

        Address address = addressRepo.findByUnitAddressIgnoreCaseAndStreetIgnoreCaseAndLandmarkIgnoreCaseAndCityIgnoreCaseAndStateIgnoreCaseAndCountryAndPincode(
                userAddress.getUnitAddress(), userAddress.getStreet(), userAddress.getLandmark(), userAddress.getCity(), userAddress.getState(),
                country, userAddress.getPincode())
                .orElseGet(() -> {
                    return addressRepo.save(new Address(
                            userAddress.getUnitAddress(),
                            userAddress.getStreet(),
                            userAddress.getLandmark(),
                            userAddress.getCity(),
                            userAddress.getState(),
                            country,
                            userAddress.getPincode()
                    ));
                });

        if(userAddressRepo.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhoneNumberAndUserAndAddress(
                userAddress.getFirstName(),
                userAddress.getLastName(),
                userAddress.getPhoneNumber(),
                user,
                address
        )) {
            throw new DuplicateAddressException("User Address already exists.");
        }
        UserAddress savedUserAddress = userAddressRepo.save(new UserAddress(
                userAddress.getFirstName(),
                userAddress.getLastName(),
                userAddress.getPhoneNumber(),
                user,
                address,
                userAddress.getIsDefault()
        ));
        return savedUserAddress.getId();
    }

    public Long updateUserAddress(String userEmail, Long addressId, UserAddressDto latestUserAddress) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        List<UserAddress> userAddresses = userAddressRepo.findByUser(user);
        if(userAddresses == null) {
            throw new EntityNotFoundException("User has no addresses.");
        }
        UserAddress matchedUserAddress = userAddresses.stream()
                .filter((userAddress) -> Objects.equals(userAddress.getId(), addressId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Address doesn't exist with user: " + user.getEmail()));

        Address matchedAddress = matchedUserAddress.getAddress();

        matchedAddress.setUnitAddress(latestUserAddress.getUnitAddress() != null && !latestUserAddress.getUnitAddress().isEmpty() ? latestUserAddress.getUnitAddress() : matchedAddress.getUnitAddress());
        matchedAddress.setStreet(latestUserAddress.getStreet() != null && !latestUserAddress.getStreet().isEmpty() ? latestUserAddress.getStreet() : matchedAddress.getStreet());
        matchedAddress.setLandmark(latestUserAddress.getLandmark() != null && !latestUserAddress.getLandmark().isEmpty() ? latestUserAddress.getLandmark() : matchedAddress.getLandmark());
        matchedAddress.setCity(latestUserAddress.getCity() != null && !latestUserAddress.getCity().isEmpty() ? latestUserAddress.getCity() : matchedAddress.getCity());
        matchedAddress.setState(latestUserAddress.getState() != null && !latestUserAddress.getState().isEmpty() ? latestUserAddress.getState() : matchedAddress.getState());
        matchedAddress.setPincode(latestUserAddress.getPincode() != null && !latestUserAddress.getPincode().isEmpty() ? latestUserAddress.getPincode() : matchedAddress.getPincode());

        Address updatedAddress = addressRepo.save(matchedAddress);

        if (updatedAddress == null) {
            throw new RuntimeException("Update address failed.");
        }
        matchedUserAddress.setFirstName(latestUserAddress.getFirstName() != null && !latestUserAddress.getFirstName().isEmpty() ? latestUserAddress.getFirstName() : matchedUserAddress.getFirstName());
        matchedUserAddress.setLastName(latestUserAddress.getLastName() != null && !latestUserAddress.getLastName().isEmpty() ? latestUserAddress.getLastName() : matchedUserAddress.getLastName());
        matchedUserAddress.setPhoneNumber(latestUserAddress.getPhoneNumber() != null && !latestUserAddress.getPhoneNumber().isEmpty() ? latestUserAddress.getPhoneNumber() : matchedUserAddress.getPhoneNumber());

        if (latestUserAddress.getIsDefault()) {
            userAddresses.stream()
                    .filter((userAddress) -> userAddress.getIsDefault())
                    .forEach((userAddress) -> userAddress.setIsDefault(false));
        }
        matchedUserAddress.setIsDefault(latestUserAddress.getIsDefault());

        UserAddress updatedUserAddress = userAddressRepo.save(matchedUserAddress);

        if (updatedUserAddress == null) {
            throw new RuntimeException("Update User address failed");
        }
        return updatedUserAddress.getId();
    }

    public void deleteUserAddress(String userEmail, Long addressId) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
        List<UserAddress> userAddresses = userAddressRepo.findByUser(user);
        if (userAddresses == null) {
            throw new EntityNotFoundException("User has no addresses");
        }
        UserAddress matchedUserAddress = userAddresses.stream()
                .filter((userAddress) -> userAddress.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Address doesn't exist with user: " + user.getEmail()));

        userAddressRepo.deleteById(matchedUserAddress.getId());
    }
}
