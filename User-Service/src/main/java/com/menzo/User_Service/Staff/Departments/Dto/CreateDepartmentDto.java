package com.menzo.User_Service.Staff.Departments.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDepartmentDto {

    @NotBlank(message = "Department Name is required.")
    private String departmentName;

    @NotBlank(message = "Department Code is required.")
    private String departmentCode;

    private String description;

    private UUID departmentHeadId;

    /////////////////////////////////////

    public String toString() {
        return "CreateDepartmentDto:\ndepartmentName: " + departmentName +
                "\ndepartmentCode: " + departmentCode +
                "\ndescription: " + description +
                "\ndepartmentHeadId: " + departmentHeadId + "\n";
    }
}
