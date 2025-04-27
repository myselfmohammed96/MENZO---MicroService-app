package com.menzo.User_Service.Service;

import com.menzo.User_Service.Dto.EmailDto;
import com.menzo.User_Service.Dto.PasswordDto;
import com.menzo.User_Service.Dto.RegNewUser;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Enums.Roles;
import com.menzo.User_Service.Feign.AuthFeign;
import com.menzo.User_Service.Feign.EmailFeign;
import com.menzo.User_Service.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    EmailFeign emailFeign;

    @Autowired
    AuthFeign authFeign;

//    Saving new Registered user

    public void saveNewUser(RegNewUser newUser){
        System.out.println("Password from the saveNewUser method: " + newUser.getPassword());
        newUser.setPassword(encodePassword(newUser.getPassword()));
        User user = new User(newUser.getFirstName(), newUser.getLastName(),
                newUser.getPhoneNumber(), newUser.getEmail(),
                newUser.getDateOfBirth(), newUser.getGender(), newUser.getPassword());
        user.setRoles(Roles.USER);
        user.setActive(true);
        userRepo.save(user);
//        printStuff(newUser);
    }

    private String encodePassword(String userPassword){
//        PasswordDto passwordDto = new PasswordDto(userPassword);
        System.out.println("Password from the user service layer..." + userPassword);
//        System.out.println("Password from the user service layer.. but from the password Dto object: " + passwordDto.getPassword());
        return authFeign.encodePassword(new PasswordDto(userPassword));
    }
//    public void saveNewUser(RegNewUser newUser) {
//
////        newUser.setDateOfBirth(LocalDate.of(2004, 2, 3));
////        newUser.setUserRole(Roles.USER);
////        newUser.setActive(true);
////        userRepo.save(newUser);"Profile Pic: " + newUser.getProfilePic() +
//        printStuff(newUser);
////        emailFeign.otpVerification(newUser.getEmail());
//
//    }

    public void printStuff(RegNewUser newUser){
        System.out.println("Hello hello.. mic testing...!!!");
        System.out.println("full name: " + newUser.getFirstName() + " " + newUser.getLastName());
        System.out.println("Email: " + newUser.getEmail() + "\nPhone number: " + newUser.getPhoneNumber());
        System.out.println("\nDate of Birth: " + newUser.getDateOfBirth() + "\nGender: " + newUser.getGender());
        System.out.println("password: " + newUser.getPassword());
    }

    public User getUserbyEmail(EmailDto userEmail) {
        User user = userRepo.findByEmail(userEmail.getEmail());
        return user;
    }
}
