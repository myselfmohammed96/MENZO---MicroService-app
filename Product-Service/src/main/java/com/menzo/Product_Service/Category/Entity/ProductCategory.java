package com.menzo.Product_Service.Category.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.menzo.Product_Service.Variation.Entity.Variation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(
        exclude = "variations"
)
@Table(
        name = "product_categories"
)
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
@FilterDef(
        name = "activeFilter",
        parameters = @ParamDef(
                name = "isDeleted",
                type = Boolean.class
        )
)
@Filter(
        name = "activeFilter",
        condition = "is_deleted = :isDeleted"
)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_category_id")
    private Long parentCategoryId;

    @Column(
            name = "category_name",
            nullable = false,
            unique = true,
            length = 100
    )
    private String categoryName;

    @Column(
            name = "abbreviation",
            unique = true
    )
    private String abbreviation;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_variation_configuration",
            joinColumns = @JoinColumn(
                    name = "category_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns =  @JoinColumn(
                    name = "variation_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Variation> variations = new HashSet<>();

    @Column(
            name = "is_active",
            nullable = false
    )
    private Boolean isActive;

    @Column(
            name = "is_deleted",
            nullable = false
    )
    private Boolean isDeleted;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updateAt = LocalDateTime.now();
    }

}
