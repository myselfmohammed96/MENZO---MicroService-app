package com.menzo.Product_Service.Variation.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "variations")
//@ToString(exclude = "options")
//@FilterDef(
//        name = "variationActiveFilter",
//        parameters = @ParamDef(
//                name = "isDeleted",
//                type = Boolean.class
//        )
//)
//@Filter(
//        name = "variationActiveFilter",
//        condition = "is_deleted = :isDeleted"
//)
public class Variation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variationId;

    @Column(nullable = false, unique = true)
    private String variationName;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id", referencedColumnName = "variationId")
    private Set<VariationOption> options = new HashSet<>();

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