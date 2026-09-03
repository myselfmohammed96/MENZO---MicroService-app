package com.menzo.Product_Service.Configuration.AdminPreferences.inventoryPreferences.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.Product_Service.Configuration.AdminPreferences.inventoryPreferences.Enum.ConfigurationLevel;
import com.menzo.Product_Service.Configuration.AdminPreferences.inventoryPreferences.Enum.PreferenceValueType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "inventory_preferences",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_preference",
                columnNames = "preference_key"
        )
)
public class InventoryPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID preferenceId;

    @Column(nullable = false)
    private String preferenceKey;

    private String preferenceValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreferenceValueType valueTypes;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConfigurationLevel configLevel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}






//DEFAULT_PRODUCT_MARGIN | NULL | PERCENTAGE
//DEFAULT_TAX_RATE       | NULL | PERCENTAGE
//DEFAULT_CURRENCY       | NULL | STRING
