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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_categories")
//@ToString(exclude = "variations")
//@JsonIgnoreProperties({
//        "hibernateLazyInitializer",
//        "handler"
//})
@FilterDef(
        name = "categoryActiveFilter",
        parameters = @ParamDef(
                name = "isDeleted",
                type = Boolean.class
        )
)
@Filter(
        name = "categoryActiveFilter",
        condition = "is_deleted = :isDeleted"
)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private ProductCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<ProductCategory> subCategories;

    @Column(nullable = false, unique = true, length = 100)
    private String categoryName;

    @Column(unique = true)
    private String abbreviation;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_variation_configuration",
            joinColumns = @JoinColumn(
                    name = "category_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "variation_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Variation> variations = new HashSet<>();

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean isDeleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
