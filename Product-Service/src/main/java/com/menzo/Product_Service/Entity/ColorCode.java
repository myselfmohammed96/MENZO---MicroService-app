package com.menzo.Product_Service.Entity;

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
@Table(name = "color_code")
public class ColorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "color_option_id"
    )
    private VariationOption colorOption;

    @Column(
            nullable = false,
            unique = true,
            name = "color_code"
    )
    private String colorCode;

    @Column(
            nullable = false,
            unique = true,
            name = "color_abbreviation"
    )
    private String colorAbbreviation;

}
