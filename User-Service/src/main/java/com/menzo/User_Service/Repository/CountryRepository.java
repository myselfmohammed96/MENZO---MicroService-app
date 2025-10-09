package com.menzo.User_Service.Repository;

import com.menzo.User_Service.Entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    public Optional<Country> findByCountryNameIgnoreCase(String country);
}
