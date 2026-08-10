package com.menzo.User_Service.Staff.Departments.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Staff.StaffProfile.Entity.Staff;
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
        name = "departments",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_department_code",
                columnNames = "department_code"
        )
)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID departmentId;

    @Column(nullable = false)
    private String departmentName;

    @Column(nullable = false)
    private String departmentCode;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(
            name = "department_head_id",
            foreignKey = @ForeignKey(name = "fk_department_head")
    )
    private Staff departmentHead;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(
            name = "deleted_by",
            foreignKey = @ForeignKey(name = "fk_department_deleted_by")
    )
    private Staff deletedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    ////////////////////////////////////////////

    public String toString() {
        String head = departmentHead != null
                ? departmentHead.getUser().getFirstName() + " " + departmentHead.getUser().getLastName()
                : "N/A";

        return "Department:\ndepartmentId: " + departmentId +
                "\ndepartmentName: " + departmentName +
                "\ndepartmentCode: " + departmentCode +
                "\ndepartmentHead: " + head +
                "\nisActive: " + isActive + "\n";
    }
}
