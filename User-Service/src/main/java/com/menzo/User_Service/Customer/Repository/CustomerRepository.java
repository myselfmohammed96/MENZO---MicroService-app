package com.menzo.User_Service.Customer.Repository;

import com.menzo.User_Service.Customer.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
