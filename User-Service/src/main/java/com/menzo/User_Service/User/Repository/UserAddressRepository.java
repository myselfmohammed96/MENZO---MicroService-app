package com.menzo.User_Service.Repository;

import com.menzo.User_Service.Entity.Address;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.StringJoiner;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    public boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhoneNumberAndUserAndAddress(String firstName, String lastname, String phoneNumber, User user, Address address);

    public List<UserAddress> findByUser(User user);
}
