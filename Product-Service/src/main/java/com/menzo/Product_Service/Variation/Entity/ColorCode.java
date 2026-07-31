package com.menzo.Product_Service.Variation.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ToString(exclude = "colorOption")
@Table(name = "color_code")
public class ColorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long colorCodeId;

    @OneToOne
    @JoinColumn(name = "color_option_id", nullable = false, unique = true)
    @JsonIgnore
    private VariationOption colorOption;

    @Column(nullable = false, unique = true)
    private String colorHexCode;

    @Column(nullable = false, unique = true, name = "color_abbreviation")
    private String colorAbbreviation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}





//    @Override
//    public int hashCode() {
//        return Objects.hash(
//                id,
//                colorCode
//        );
//    }