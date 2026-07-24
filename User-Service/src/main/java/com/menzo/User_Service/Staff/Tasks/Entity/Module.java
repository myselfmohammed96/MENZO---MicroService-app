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
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Column(nullable = false, unique = true)
    private String moduleCode;

    @Column(nullable = false)
    private String moduleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    //  revise
    private Integer displayOrder;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    ////////////////////////////////////////

    public String toString() {
        return "Module:\nmoduleId: " + moduleId +
                "\nmoduleCode: " + moduleCode +
                "\nmoduleName: " + moduleName +
                "\ndisplayOrder: " + displayOrder +
                "\nisActive: " + isActive +
                "\ncreatedAt: " + createdAt + "\n";
    }
}
