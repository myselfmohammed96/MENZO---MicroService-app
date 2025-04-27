package com.menzo.User_Service.Repository;

import com.menzo.User_Service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);
}
