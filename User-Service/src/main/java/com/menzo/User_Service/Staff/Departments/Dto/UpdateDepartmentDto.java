package com.menzo.User_Service.Staff.Departments.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDepartmentDto {

    private String departmentName;

    private String departmentCode;

    private String description;

    ////////////////////////////////////

    public String toString() {
        return "UpdateDepartmentDto:\ndepartmentName: " + departmentName +
                "\ndepartmentCode: " + departmentCode +
                "\ndescription: " + description + "\n";
    }
}
