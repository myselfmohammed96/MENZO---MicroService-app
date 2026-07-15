package com.menzo.User_Service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String unitAddress;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false)
    private String pincode;

    private String landmark;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    ////////////////////////////////////////////////////////////////////

    public Address(String unitAddress, String street, String landmark,
                   String city, String state, Country country, String pincode) {
        this.unitAddress = unitAddress;
        this.street = street;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }

    public Address(Long id, String unitAddress, String street,
                          String landmark, String city, String state,
                          Country country, String pincode) {
        this.id = id;
        this.unitAddress = unitAddress;
        this.street = street;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }

    public String toString() {
        return "\nUserAddressDto:\nid: " + id +
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
