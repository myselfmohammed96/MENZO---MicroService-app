package com.menzo.User_Service.Staff.Tasks.Entity;

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
        name = "tasks",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_task_code",
                columnNames = "task_code"
        )
)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID taskId;

    @Column(nullable = false)
    private String taskCode;

    @Column(nullable = false)
    private String taskName;

    @ManyToOne
    @JoinColumn(
            name = "module_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_task_module")
    )
    private Module module;

    private String description;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /////////////////////////////////////////

    public String toString() {
        return "Task:\ntaskId: " + taskId +
                "\ntaskCode: " + taskCode +
                "\ntaskName: " + taskName +
                "\nmodule: " + module.getModuleName() +
                "\ndescription: " + description +
                "\nisActive: " + isActive +
                "\ncreatedAt: " + createdAt + "\n";
    }

}
