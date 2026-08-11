package com.menzo.Product_Service.Variation.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "variations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_variation_name",
                columnNames = "variation_name"
        )
)
//@ToString(exclude = "options")
@FilterDef(
        name = "variationFilter",
        parameters =  {
                @ParamDef(name = "applyActive", type = Boolean.class),
                @ParamDef(name = "isActive", type = Boolean.class),
                @ParamDef(name = "applyDeleted", type = Boolean.class),
                @ParamDef(name = "isDeleted", type = Boolean.class)
        }
)
@Filter(
        name = "variationFilter",
        condition = "(:applyActive = false OR is_active = :isActive)" +
                "AND (:applyDeleted = false OR is_deleted = :isDeleted"
)
public class Variation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID variationId;

    @Column(nullable = false)
    private String variationName;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id", referencedColumnName = "variationId")
    private Set<VariationOption> options = new HashSet<>();

    ////////////////////////////////////////////////////////

//    @Override
//    public int hashCode() {
//        return Objects.hash(
//                id,
//                variationName
//        );
//    }

}





//    @JsonIgnore
//    @ManyToMany(mappedBy = "variations")
//    private Set<ProductCategory> categories = new HashSet<>();





//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Variation)) return false;
//        Variation that = (Variation) o;
//        return Objects.equals(id, that.id);
//    }
//