package com.menzo.Product_Service.Category.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "product_categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_category_name",
                        columnNames = "category_name"
                ),
                @UniqueConstraint(
                        name = "uk_category_abbreviation",
                        columnNames = "abbreviation"
                )
        }
)
//@ToString(exclude = "variations")
//@JsonIgnoreProperties({
//        "hibernateLazyInitializer",
//        "handler"
//})
@FilterDef(
        name = "categoryFilter",
        parameters = {
                @ParamDef(name = "isActive", type = Boolean.class),
                @ParamDef(name = "isDeleted", type = Boolean.class)
        }
)
@Filter(
        name = "categoryFilter",
        condition = "is_active = :isActive AND is_deleted = :isDeleted"
)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private UUID categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "parent_category_id",
            foreignKey = @ForeignKey(name = "fk_parent_category")
    )
    private ProductCategory parentCategory;

    @Column(nullable = false, length = 100)
    private String categoryName;

    private String abbreviation;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "parentCategory")
    private List<ProductCategory> subCategories;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_variation_configuration",
            joinColumns = @JoinColumn(
                    name = "category_id",
                    referencedColumnName = "category_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "variation_id",
                    referencedColumnName = "variation_id"
            )
    )
    private Set<Variation> variations = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
