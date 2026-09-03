package com.menzo.User_Service.User.Address.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "countries",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_country",
                columnNames = "country_name"
        )
)
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID countryId;

    @Column(nullable = false)
    private String countryName;

    ////////////////////////////////////

    public String toString() {
        return "Country:\ncountryId: " + countryId +
                "\ncountryName: " + countryName + "\n";
    }
}
