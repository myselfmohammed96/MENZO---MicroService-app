package com.menzo.Product_Service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "variation_options", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"variation_id", "option_value"})
})
public class VariationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "option_value")
    private String optionValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id", nullable = false)
    @JsonIgnore
    private Variation variation;
//    @Column(nullable = false, name = "variation_id")
//    private Long variationId;

    @Column(nullable = false, name = "created_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private Date createdAt;

//    @OneToMany(mappedBy = "variationOption", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductConfiguration> configurations = new ArrayList<>();

    public VariationOption(){}

    public VariationOption(String optionValue, Variation variation){
        this.optionValue = optionValue;
        this.variation = variation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Variation getVariation() {
        return variation;
    }

    public void setVariation(Variation variation) {
        this.variation = variation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String toString() {
        return "VariationOption:\nid: " + id + "\noptionValue: " + optionValue +
                "\nvariationId: " + variation + "\ncreatedAt: " + createdAt;
    }
}
