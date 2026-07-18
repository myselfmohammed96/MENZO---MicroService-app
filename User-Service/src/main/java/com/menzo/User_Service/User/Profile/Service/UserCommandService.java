package com.menzo.User_Service.User.Profile.Service;

import com.menzo.User_Service.User.Profile.Dto.BlockUserDto;
import com.menzo.User_Service.User.Profile.Dto.UserDto;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserCommandService {

    private static final Logger logger = LoggerFactory.getLogger(UserCommandService.class);

    @Autowired
    private UserRepository userRepo;



    /*
    *
    *   Update user active status
    *
    */
    public void updateUserActiveStatus(BlockUserDto blockUserDto) {
        try {
            User user = userRepo.findById(blockUserDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + blockUserDto.getUserId()));
            if (user.isActive() != blockUserDto.isBlock()) {
                throw new RuntimeException("Invalid Input, block request mismatch.");
            }
            user.setActive(user.isActive() && blockUserDto.isBlock() ? false : true);
            userRepo.save(user);
        } catch (Exception e) {
            logger.error("Error updating user active status: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating user activeStatus", e);
        }
    }



    /*
    *
    *   Update user details
    *
    */
    public Long updateUserDetails(String userEmail, UserDto latestUser) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Email: " + userEmail));

        User updatedUser;
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
}
