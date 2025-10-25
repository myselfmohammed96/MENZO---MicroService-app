package com.menzo.Product_Service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "variations")
public class Variation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            name = "variation_name"
    )
    private String variationName;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "variation_id",
            referencedColumnName = "id"
    )
    private Set<VariationOption> options = new HashSet<>();

    @Column(
            nullable = false,
            name = "created_at"
    )
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy"
    )
    @CreationTimestamp
    private Date createdAt;

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                variationName
        );
    }

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