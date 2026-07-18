package com.menzo.User_Service.User.Address.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "country_name")
    private String countryName;

    ////////////////////////////////////

    public String toString() {
        return "Country:\nid: " + id +
                "\ncountryName: " + countryName + "\n";
    }
}
