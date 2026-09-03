package com.menzo.User_Service.Staff.Permissions.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Staff.Permissions.Enum.PermissionType;
import com.menzo.User_Service.Staff.StaffProfile.Entity.Staff;
import com.menzo.User_Service.Staff.Tasks.Entity.Task;
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
        name = "special_permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_special_permission_task",
                        columnNames = {"staff_id", "task_id"}
                )
        }
)
public class SpecialPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID permissionId;

    @ManyToOne
    @JoinColumn(
            name = "staff_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_staff_special_permission")
    )
    private Staff staff;

    @ManyToOne
    @JoinColumn(
            name = "task_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_special_permission")
    )
    private Task task;

    @Column(nullable = false)
    private PermissionType permissionType;

    private String reason;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_special_permission_granted_by")
    )
    private Staff grantedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime validFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime validUntil;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_special_permission_revoked_by")
    )
    private Staff revokedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime revokedAt;

    ////////////////////////////////////////////

    public String toString() {
        return "SpecialPermission:\npermissionId: " + permissionId +
                "\nstaff: " + staff.getUser().getFirstName() + " " + staff.getUser().getLastName() +
                "\ntask: " + task.getTaskName() +
                "\npermissionType: " + permissionType +
                "\nreason: " + reason +
                "\ngrantedBy: " + grantedBy.getUser().getFirstName() + " " + grantedBy.getUser().getLastName() +
                "\nvalidFrom: " + validFrom +
                "\nvalidUntil: " + validUntil +
                "\nisActive: " + isActive +
                "\ncreatedAt: " + createdAt +
                "\nrevokedBy: " + revokedBy + "\n";
    }

}
