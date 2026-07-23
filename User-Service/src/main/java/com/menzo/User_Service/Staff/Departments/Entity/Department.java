package com.menzo.User_Service.Staff.Departments.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Staff.StaffProfile.Entity.Staff;
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
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(nullable = false)
    private String departmentName;

    @Column(nullable = false, unique = true)
    private String departmentCode;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "department_head_id")
    private Staff departmentHead;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private Staff deletedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
