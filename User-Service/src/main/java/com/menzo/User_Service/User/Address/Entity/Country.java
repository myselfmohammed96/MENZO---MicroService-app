package com.menzo.User_Service.User.Address.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer countryId;

    @Column(nullable = false, unique = false)
    private String countryName;

    ////////////////////////////////////

    public String toString() {
        return "Country:\ncountryId: " + countryId +
                "\ncountryName: " + countryName + "\n";
    }
}
