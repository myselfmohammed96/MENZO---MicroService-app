package com.menzo.User_Service.Staff.Departments.Service;

import com.menzo.User_Service.Exceptions.DuplicateEntityException;
import com.menzo.User_Service.Exceptions.ResourceAlreadyDeletedException;
import com.menzo.User_Service.GlobalComponents.Enum.ChangeStatus;
import com.menzo.User_Service.Staff.Departments.Dto.CreateDepartmentDto;
import com.menzo.User_Service.Staff.Departments.Dto.UpdateDepartmentDto;
import com.menzo.User_Service.Staff.Departments.Entity.Department;
import com.menzo.User_Service.Staff.Departments.Repository.DepartmentRepository;
import com.menzo.User_Service.Staff.StaffProfile.Entity.Staff;
import com.menzo.User_Service.Staff.StaffProfile.Service.StaffQueryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepartmentCommandService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentCommandService.class);

    @Autowired
    private DepartmentRepository deptRepo;

    @Autowired
    private StaffQueryService staffQueryService;


    /*
     *
     *   Add new department
     *
     */
    public Department addNewDepartment(@Valid CreateDepartmentDto newDept) {

        //  duplicate existence - validation
        if (deptRepo.existsByDepartmentName(newDept.getDepartmentName())
                || deptRepo.existsByDepartmentCode(newDept.getDepartmentCode())) {
            logger.error("Department '{}' already exists", newDept.getDepartmentName());
            throw new DuplicateEntityException("Department already exists.");
        }

        //  Saving new department
        logger.info("Saving new department: {}", newDept.getDepartmentName());
        return deptRepo.save(Department.builder()
                .departmentName(newDept.getDepartmentName())
                .departmentCode(newDept.getDepartmentCode())
                .description(newDept.getDescription())
                .departmentHead(staffQueryService.getHeadStaffById(newDept.getDepartmentHeadId()))
                .build()
        );
    }


    /*
     *
     *   Update department
     *
     */
    public Department updateDepartment(Long deptId, UpdateDepartmentDto latestDept) {

        //  fetching department by ID
        Department dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + deptId));

        //  updating department
        dept.setDepartmentName(latestDept.getDepartmentName() != null
                ? latestDept.getDepartmentName()
                : dept.getDepartmentName()
        );
        dept.setDepartmentCode(latestDept.getDepartmentCode() != null
                ? latestDept.getDepartmentCode()
                : dept.getDepartmentCode()
        );
        dept.setDescription(latestDept.getDescription() != null
                ? latestDept.getDescription()
                : dept.getDescription()
        );
        return deptRepo.save(dept);
    }


    /*
     *
     *  Update department active status
     *  Department identified by department ID
     *
     */
    public boolean updateDepartmentActiveStatus(Long deptId, boolean isActive) {

        //  fetching department by ID
        Department dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + deptId));

        //  updating department active status
        dept.setActive(isActive);
        return deptRepo.save(dept).isActive();
    }


    /*
     *
     *  Change department head
     *  Department identified by department ID
     *  Department head identified by staff ID
     *
     */
    public ChangeStatus changeDepartmentHead(Long deptId, Long deptHeadId) {

        //  fetching data
        Department dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + deptId));
        Staff newDeptHead = staffQueryService.getHeadStaffById(deptHeadId);

        //  changing department head
        if (dept.getDepartmentHead() == null ||
                !newDeptHead.getStaffId().equals(dept.getDepartmentHead().getStaffId())) {
            dept.setDepartmentHead(newDeptHead);
            deptRepo.save(dept);
            return ChangeStatus.CHANGED;
        }
        return ChangeStatus.NO_CHANGE;
    }


    /*
     *
     *  Delete department
     *  Department identified by department ID
     *  (soft delete)
     *
     */
    public boolean deleteDepartment(Long deptId, String authorEmail) {

        //  fetching data
        Department dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + deptId));
        Staff author = staffQueryService.getStaffByEmail(authorEmail);

        if (dept.isDeleted()) {
            throw new ResourceAlreadyDeletedException("Department already deleted.");
        }

        //  deleting department (soft delete)
        dept.setDeleted(true);
        dept.setDeletedAt(LocalDateTime.now());
        dept.setDeletedBy(author);
        return deptRepo.save(dept).isDeleted();
    }
}
