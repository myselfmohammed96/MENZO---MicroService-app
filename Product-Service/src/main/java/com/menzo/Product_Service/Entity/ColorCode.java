package com.menzo.Product_Service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "colorOption")
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
    @JsonIgnore
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
