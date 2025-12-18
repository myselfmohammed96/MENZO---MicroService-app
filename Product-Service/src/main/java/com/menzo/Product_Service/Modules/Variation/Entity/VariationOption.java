package com.menzo.Product_Service.Modules.Variation.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"variation", "colorCode"})
@Table(
        name = "variation_options",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "variation_id",
                                "option_value"
                        }
                )
})
@FilterDef(
        name = "optionActiveFilter",
        parameters = @ParamDef(
                name = "isDeleted",
                type = Boolean.class
        )
)
@Filter(
        name = "optionActiveFilter",
        condition = "is_deleted = :isDeleted"
)
public class VariationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            name = "option_value"
    )
    private String optionValue;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "variation_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Variation variation;

    @OneToOne(
            mappedBy = "colorOption",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private ColorCode colorCode;

    @Column(
            name = "is_deleted",
            nullable = false
    )
    private Boolean isDeleted;

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

}







//    @OneToMany(mappedBy = "variationOption", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductConfiguration> configurations = new ArrayList<>();