package com.menzo.User_Service.User.Address.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
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
        name = "addresses",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_address",
                columnNames = {
                        "country_id",
                        "state",
                        "city",
                        "street",
                        "unit_address"
                }
        )
)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    private String unitAddress;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @ManyToOne
    @JoinColumn(
            name = "country_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_country")
    )
    private Country country;

    @Column(nullable = false)
    private String pincode;

    private String landmark;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    ////////////////////////////////////////////////////////////////////

    public String toString() {
        return "\nUserAddressDto:\naddressId: " + addressId +
                "\nunitAddress: " + unitAddress +
                "\nstreet: " + street +
                "\nlandmark: " + landmark +
                "\ncity: " + city +
                "\nstate: " + state +
                "\ncountry: " + country +
                "\npincode: " + pincode +
                "\ncreatedAt: " + createdAt +
                "\nupdatedAt: " + updatedAt + "\n";
    }
}
