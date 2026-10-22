package com.menzo.User_Service.User.Profile.Repository;

import com.menzo.User_Service.User.Profile.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailIgnoreCase(String email);

//    boolean existsByPhoneNumber(String phoneNumber);









//    ---------------------------------------------------------
//    Optional<User> findByEmail(String email);


    /*
     *
     *   Check if user email already exists
     *
     */
//    boolean existsByEmail(String email);


    /*
     *
     *   Check if verified user with this email exist
     *
     */
//    boolean existsByEmailAndIsVerifiedTrue(String email);

}
