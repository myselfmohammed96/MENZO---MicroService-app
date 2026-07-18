package com.menzo.User_Service.User.Address.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.User.Address.Entity.Country;
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
    private String decode;

    private String landmark;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    ////////////////////////////////////////////////////////////////////

    public String toString() {
        return "\nUserAddressDto:\nid: " + id +
                "\nunitAddress: " + unitAddress +
                "\nstreet: " + street +
                "\nlandmark: " + landmark +
                "\ncity: " + city +
                "\nstate: " + state +
                "\ncountry: " + country +
                "\ndecode: " + decode +
                "\ncreatedAt: " + createdAt +
                "\nupdatedAt: " + updatedAt + "\n";
    }
}
