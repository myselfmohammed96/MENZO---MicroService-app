package com.menzo.Product_Service.Variation.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ToString(exclude = {"variation", "colorCode"})
@Table(
        name = "variation_options",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_variation_option",
                columnNames = {
                        "variation_id",
                        "option_value"
                }
        )
)
@FilterDef(
        name = "optionFilter",
        parameters = {
                @ParamDef(name = "isActive", type = Boolean.class),
                @ParamDef(name = "isDeleted", type = Boolean.class)
        }
)
@Filter(
        name = "optionFilter",
        condition = "is_active = :isActive AND is_deleted = :isDeleted"
)
public class VariationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID optionId;

    @Column(nullable = false)
    private String optionValue;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "variation_id",
            referencedColumnName = "variationId",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_option_variation")
    )
    private Variation variation;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "colorOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private ColorCode colorCode;

}


//    @OneToMany(mappedBy = "variationOption", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductConfiguration> configurations = new ArrayList<>();