package com.menzo.User_Service.Staff.Departments.Entity;

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
@Table(name = "designations")
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer designationId;

    @Column(nullable = false, unique = true)
    private String designationName;

    private String description;

    @Column(unique = true)
    private Integer level;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isCoreDesignation;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
