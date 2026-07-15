package com.menzo.User_Service.Repository;

import com.menzo.User_Service.Entity.Address;
import com.menzo.User_Service.Entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    public Optional<Address> findByUnitAddressIgnoreCaseAndStreetIgnoreCaseAndLandmarkIgnoreCaseAndCityIgnoreCaseAndStateIgnoreCaseAndCountryAndPincode(
            String unitAddress, String street, String landmark, String city, String state, Country country, String pincode);
}
