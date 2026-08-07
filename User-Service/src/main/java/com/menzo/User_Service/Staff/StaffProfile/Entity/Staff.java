package com.menzo.User_Service.Staff.StaffProfile.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Staff.Departments.Entity.Department;
import com.menzo.User_Service.Staff.Departments.Entity.Designation;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "staffs")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate joiningDate;

    @ManyToOne
    @JoinColumn(name = "superior_id")
    private Staff superior;

    //////////////////////////////////////////////

    public String toString() {
        return "Staff:\nstaffId: " + staffId +
                "\nuser: " + user.getFirstName() + " " + user.getLastName() +
                "\ndepartment: " + department.getDepartmentName() +
                "\ndesignation: " + designation.getDesignationName() +
                "\njoiningDate: " + joiningDate +
                "\nsuperior: " + superior.getUser().getFirstName() + " " + superior.getUser().getLastName() + "\n";
    }

}
