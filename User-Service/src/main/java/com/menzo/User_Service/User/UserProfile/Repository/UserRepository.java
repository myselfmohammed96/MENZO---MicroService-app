package com.menzo.User_Service.User.UserProfile.Repository;

import com.menzo.User_Service.User.UserProfile.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public boolean existsByEmail(String email);

}
