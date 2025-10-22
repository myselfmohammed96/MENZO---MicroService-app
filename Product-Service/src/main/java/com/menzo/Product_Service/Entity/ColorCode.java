package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "color_code")
public class ColorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false, name = "color_option_id")
    private VariationOption colorOption;

    @Column(nullable = false, unique = true, name = "color_code")
    private String colorCode;

    @Column(nullable = false, unique = true, name = "color_abbreviation")
    private String colorAbbreviation;

    public ColorCode() {}

    public ColorCode(VariationOption colorOption, String colorCode, String colorAbbreviation) {
        this.colorOption = colorOption;
        this.colorCode = colorCode;
        this.colorAbbreviation = colorAbbreviation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VariationOption getColorOption() {
        return colorOption;
    }

    public void setColorOption(VariationOption colorOption) {
        this.colorOption = colorOption;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorAbbreviation() {
        return colorAbbreviation;
    }

    public void setColorAbbreviation(String colorAbbreviation) {
        this.colorAbbreviation = colorAbbreviation;
    }
}
