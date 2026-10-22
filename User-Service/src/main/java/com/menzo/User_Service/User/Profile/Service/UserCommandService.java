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
import java.util.UUID;

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
//    public UUID updateUserDetails(String userEmail, UserDto latestUser) {
//        User user = userRepo.findByEmail(userEmail)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with Email: " + userEmail));
//
//        User updatedUser;
//        try {
//            user.setFirstName(latestUser.getFirstName() != null && !latestUser.getFirstName().isEmpty() ? latestUser.getFirstName() : user.getFirstName());
//            user.setLastName(latestUser.getLastName() != null && !latestUser.getLastName().isEmpty() ? latestUser.getLastName() : user.getLastName());
//            user.setPhoneNumber(latestUser.getPhoneNumber() != null && !latestUser.getPhoneNumber().isEmpty() ? latestUser.getPhoneNumber() : user.getPhoneNumber());
//            user.setDateOfBirth(latestUser.getDateOfBirth() != null ? latestUser.getDateOfBirth() : user.getDateOfBirth());
//            user.setGender(latestUser.getGender() != null ? latestUser.getGender() : user.getGender());
//
//            updatedUser = userRepo.save(user);
//        } catch (RuntimeException e) {
//            logger.error("Error while updating user with email: {}", userEmail);
//            throw new RuntimeException("Error while updating User with email: " + userEmail);
//        }
//        if (Objects.equals(latestUser.getFirstName(), updatedUser.getFirstName()) ||
//                Objects.equals(latestUser.getLastName(), updatedUser.getLastName()) ||
//                Objects.equals(latestUser.getPhoneNumber(), updatedUser.getPhoneNumber()) ||
//                Objects.equals(latestUser.getDateOfBirth(), updatedUser.getDateOfBirth()) ||
//                Objects.equals(latestUser.getGender(), updatedUser.getGender())) {
//            return updatedUser.getUserId();
//        } else {
//            return null;
//        }
//    }


//    private final Random random = new Random();
//    @Autowired
//    private AuthFeign authFeign;
//    @PostConstruct
//    public void init() {
//        String[] names = {"Elias Wren", "Marcus Idogun", "Théo Bastien", "Kian Novak", "Rafael Duarte", "Julian Moss", "Omar Salih", "Sebastian Kade", "Leo Marchetti", "Arjun Mehta", "Felix Okafor", "Dominic Reyes", "Noah Lindqvist", "Tomas Vega", "Adrian Voss", "Kofi Amankwah", "Luca Fontane", "Hugo Bellamy", "Nikolai Sorenson", "Mateo Rossi"};
//
//        for (String name : names) {
//            //  create User object
//            String[] nameParts = name.trim().split("\\s+");
//            String email = nameParts[0] + "." + nameParts[1] + "@example.com";
//            String phone = (char) ('6' + random.nextInt(4)) + String.format("%09d", random.nextInt(1_000_000_000));
//            String date = (1 + random.nextInt(28))
//                    + "-" + (1 + random.nextInt(12))
//                    + "-" + (1985 + random.nextInt(36));
//            LocalDate localDate = LocalDate.parse(date,
//                    DateTimeFormatter.ofPattern("d-M-y"));
//            String password = authFeign.encodePassword(new HashDto(email)).getString();
//
//            User user = User.builder()
//                    .firstName(nameParts[0])
//                    .lastName(nameParts[1])
//                    .email(email.toLowerCase())
//                    .phoneNumber(phone)
//                    .gender(Gender.MALE)
//                    .userType(UserTypes.CUSTOMER)
//                    .dateOfBirth(localDate)
//                    .password(password)
//                    .isVerified(true)
//                    .userRegistrationSource(UserRegistrationSource.USER_SIGN_IN)
//                    .build();
//            User savedUser = userRepo.save(user);
//
//            //  create customer object
//            Customer customer = Customer.builder()
//                    .user(savedUser)
//                    .build();
//            Customer savedCust = cusRepo.save(customer);
//
//            //  create cart object
//            cartRepo.save(Cart.builder().customer(savedCust).build());
//
//            //  create wishlist object
//            wishRepo.save(Wishlist.builder().customer(savedCust).build());
//        }
//    }
//    @Autowired
//    private CustomerRepository cusRepo;
//
//    @Autowired
//    private CartRepository cartRepo;
//
//    @Autowired
//    private WishlistRepository wishRepo;
}
