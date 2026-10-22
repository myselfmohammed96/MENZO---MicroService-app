package com.menzo.User_Service.User.Customer.Repository;

import com.menzo.User_Service.User.Customer.Entity.Customer;
import com.menzo.User_Service.User.Profile.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByUser(User user);
}
