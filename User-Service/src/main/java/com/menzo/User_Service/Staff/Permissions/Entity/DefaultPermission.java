package com.menzo.User_Service.Staff.Permissions.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Staff.Departments.Entity.Department;
import com.menzo.User_Service.Staff.Departments.Entity.Designation;
import com.menzo.User_Service.Staff.Tasks.Entity.Task;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "default_permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_default_permission",
                        columnNames = {"department_id", "designation_id", "task_id"}
                )
        }
)
public class DefaultPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "designation_id", nullable = false)
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false)
    private boolean isAllowed = true;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /////////////////////////////////////

    public String toString() {
        return "DefaultPermission:\npermissionId: " + permissionId +
                "\ndepartment: " + department.getDepartmentName() +
                "\ndesignation: " + designation.getDesignationName() +
                "\ntask: " + task.getTaskName() +
                "\nisAllowed: " + isAllowed +
                "\ncreatedAt: " + createdAt + "\n";
    }
}
