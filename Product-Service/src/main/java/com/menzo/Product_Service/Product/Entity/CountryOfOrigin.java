package com.menzo.Product_Service.Product.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "country_of_origin")
public class CountryOfOrigin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID countryId;

    @Column(nullable = false, unique = true)
    private String countryName;

}
