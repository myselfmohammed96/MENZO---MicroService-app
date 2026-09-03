package com.menzo.User_Service.User.Address.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "user_addresses",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_address",
                columnNames = {
                        "user_id",
                        "address_id"
                }
        )
)
@SQLDelete(sql = "UPDATE user_address SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userAddressId;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_address")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "address_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_address")
    )
    private Address address;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isDefault;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
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

    //////////////////////////////////////////////////////////

    public String toString() {
        return "\nUserAddress:\nuserAddressId: " + userAddressId +
                "\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nphoneNumber: " + phoneNumber +
                "\nisDefault: " + isDefault + "\n";
    }
}
