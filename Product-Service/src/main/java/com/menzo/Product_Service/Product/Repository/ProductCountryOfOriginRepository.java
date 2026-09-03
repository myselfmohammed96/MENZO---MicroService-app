package com.menzo.Product_Service.Product.Repository;

import com.menzo.Product_Service.Product.Entity.CountryOfOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductCountryOfOriginRepository extends JpaRepository<CountryOfOrigin, UUID> {

//    public boolean existsByCountryNameIgnoreCase(String countryName);

    public Optional<CountryOfOrigin> findByCountryNameIgnoreCase(String countryName);
}
