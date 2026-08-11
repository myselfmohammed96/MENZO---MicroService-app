package com.menzo.Product_Service.Variation.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ToString(exclude = "colorOption")
@Table(
        name = "color_code",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_color_option",
                        columnNames = "color_option_id"
                ),
                @UniqueConstraint(
                        name = "uk_color_hex_code",
                        columnNames = "color_hex_code"
                ),
                @UniqueConstraint(
                        name = "uk_color_abbreviation",
                        columnNames = "color_abbreviation"
                )
        }
)
public class ColorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID colorCodeId;

    @OneToOne
    @JoinColumn(
            name = "color_option_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_color_option")
    )
    @JsonIgnore
    private VariationOption colorOption;

    @Column(nullable = false)
    private String colorHexCode;

    @Column(nullable = false)
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