package com.menzo.User_Service.Staff.Tasks.Entity;

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
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false, unique = true)
    private String taskCode;

    @Column(nullable = false)
    private String taskName;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    private String description;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
