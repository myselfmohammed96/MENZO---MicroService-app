package com.menzo.Product_Service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "variation_options", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"variation_id", "option_value"})
})
public class VariationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            name = "option_value"
    )
    private String optionValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "variation_id",
            nullable = false
    )
    @JsonIgnore
    private Variation variation;

    @OneToOne(
            mappedBy = "colorOption",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private ColorCode colorCode;

    @Column(
            nullable = false,
            name = "created_at",
            updatable = false
    )
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy"
    )
    @CreationTimestamp
    private Date createdAt;

    @Override
    public String toString() {
        return "VariationOption(id:" + id + " optionValue:" + optionValue + " colorCode:" + colorCode +
                " createdAt:" + createdAt + ")";
    }

}







//    @OneToMany(mappedBy = "variationOption", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductConfiguration> configurations = new ArrayList<>();