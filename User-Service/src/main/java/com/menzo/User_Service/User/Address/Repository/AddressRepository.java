package com.menzo.User_Service.User.Address.Repository;

import com.menzo.User_Service.User.Address.Entity.Address;
import com.menzo.User_Service.User.Address.Entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    public Optional<Address> findByUnitAddressIgnoreCaseAndStreetIgnoreCaseAndLandmarkIgnoreCaseAndCityIgnoreCaseAndStateIgnoreCaseAndCountryAndPincode(
            String unitAddress,
            String street,
            String landmark,
            String city,
            String state,
            Country country,
            String pincode
    );
}
