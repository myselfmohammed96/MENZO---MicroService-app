package com.menzo.User_Service.User.Address.Repository;

import com.menzo.User_Service.User.Address.Entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {

    public Optional<Country> findByCountryNameIgnoreCase(String country);
}
