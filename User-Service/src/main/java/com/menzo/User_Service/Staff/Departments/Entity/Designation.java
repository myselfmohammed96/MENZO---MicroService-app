package com.menzo.User_Service.Staff.Departments.Entity;

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
        name = "designations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_designation_name",
                        columnNames = "designation_name"
                ),
                @UniqueConstraint(
                        name = "uk_designation_level",
                        columnNames = "level"
                )
        }
)
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID designationId;

    @Column(nullable = false)
    private String designationName;

    private String description;

    private Integer level;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isCoreDesignation;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    ///////////////////////////////////////

    public String toString() {
        return "Designation:\ndesignationId: " + designationId +
                "\ndesignationName: " + designationName +
                "\ndescription: " + description +
                "\nlevel: " + level +
                "\nisActive: " + isActive +
                "\ncreatedAt: " + createdAt + "\n";
    }
}
