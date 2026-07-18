package com.menzo.User_Service.User.Address.Repository;

import com.menzo.User_Service.User.Address.Entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    public Optional<Country> findByCountryNameIgnoreCase(String country);
}
