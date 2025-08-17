package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "county_of_origin")
public class CountryOfOrigin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "country_name")
    private String countryName;

    public CountryOfOrigin() {}

    public CountryOfOrigin(String countryName) {
        this.countryName = countryName;
    }

    public CountryOfOrigin(Long id, String countryName) {
        this.id = id;
        this.countryName = countryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void display() {
        System.out.println("CountryOfOrigin:\nid: " + id + "\ncountryName: " + countryName);
    }
}
