package com.menzo.User_Service.User.Address.Repository;

import com.menzo.User_Service.User.Address.Entity.Address;
import com.menzo.User_Service.User.Address.Entity.UserAddress;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

    public boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhoneNumberAndUserAndAddress(
            String firstName,
            String lastname,
            String phoneNumber,
            User user,
            Address address
    );

    public List<UserAddress> findByUser(User user);
}
