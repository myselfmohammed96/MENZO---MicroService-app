package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.CountryOfOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCountryOfOriginRepo extends JpaRepository<CountryOfOrigin, Long> {

//    public boolean existsByCountryNameIgnoreCase(String countryName);

    public Optional<CountryOfOrigin> findByCountryNameIgnoreCase(String countryName);
}
