package com.menzo.User_Service.Staff.Departments.Controller;

import com.menzo.User_Service.GlobalComponents.Enum.ChangeStatus;
import com.menzo.User_Service.Staff.Departments.Dto.CreateDepartmentDto;
import com.menzo.User_Service.Staff.Departments.Dto.UpdateDepartmentDto;
import com.menzo.User_Service.Staff.Departments.Entity.Department;
import com.menzo.User_Service.Staff.Departments.Service.DepartmentCommandService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dept")
public class DepartmentCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentCommandRestController.class);

    @Autowired
    private DepartmentCommandService deptCommandService;


    /*
     *
     *   Add new department
     *
     */
    @PostMapping("/add-dept")
    public ResponseEntity<?> addDepartment(@RequestHeader("roles") String roles,
                                           @Valid @RequestBody CreateDepartmentDto newDept,
                                           BindingResult result) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage()));
                logger.warn("Validation failed for new department: {}", errors);
                return ResponseEntity.badRequest().body(errors);
            }

            //  adding department
            Department savedDept = deptCommandService.addNewDepartment(newDept);

            //  response
            if (savedDept != null) {
                logger.info("Department created successfully with ID: {}", savedDept.getDepartmentId());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Department created successfully", "departmentId", savedDept.getDepartmentId()));
            } else {
                logger.error("Department creation failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Department creation failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update department
     *
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateDepartment(@RequestHeader("roles") String roles,
                                              @RequestParam("id") Long deptId,
                                              @RequestBody UpdateDepartmentDto latestDept) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (deptId == null || deptId <= 0) {
                logger.warn("Invalid department ID: {}", deptId);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid department ID"));
            }

            //  updating department
            Department updatedDept = deptCommandService.updateDepartment(deptId, latestDept);

            //  response
            if (updatedDept != null) {
                logger.info("Department with ID {} updated successfully", deptId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Department updated successfully", "departmentId", updatedDept.getDepartmentId()));
            } else {
                logger.error("Department update failed for ID: {}", deptId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Department update failed"));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Update department active status
     *
     */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateDepartmentActiveStatus(@RequestHeader("roles") String roles,
                                                          @RequestParam("id") Long deptId,
                                                          @RequestParam("active") boolean isActive) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (deptId == null || deptId <= 0) {
                logger.warn("Invalid department ID: {}", deptId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid department ID"));
            }

            //  update active status
            boolean updatedActive = deptCommandService.updateDepartmentActiveStatus(deptId, isActive);

            //  response
            if (isActive == updatedActive) {
                logger.info("Active status for department {}, updated successfully", deptId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Department active status updated successfully"));
            } else {
                logger.error("Active status update failed for department ID: {}", deptId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Department active status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Change department head
     *
     */
    @PutMapping("/change-head")
    public ResponseEntity<?> changeDepartmentHead(@RequestHeader("roles") String roles,
                                                  @RequestParam("deptId") Long deptId,
                                                  @RequestParam("headId") Long deptHeadId) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (deptId == null || deptId <= 0) {
                logger.warn("Invalid department ID: {}", deptId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid department ID"));
            }
            if (deptHeadId == null || deptHeadId <= 0) {
                logger.warn("Invalid department Head ID: {}", deptHeadId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid department head ID"));
            }

            //  change department head
            ChangeStatus deptHeadChangeStatus = deptCommandService.changeDepartmentHead(deptId, deptHeadId);

            //  response
            if (deptHeadChangeStatus == ChangeStatus.CHANGED) {
                logger.info("Department head changed successfully for department ID: {}", deptId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Department head changed successfully."));
            }
            if (deptHeadChangeStatus == ChangeStatus.NO_CHANGE) {
                logger.info("Change not required. Same department head ID: {}", deptHeadId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Same department head selected for change."));
            } else {
                logger.error("Department head changing failed for department ID: {}", deptId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Department head changing failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Delete department
     *   (soft delete)
     *
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDepartment(@RequestHeader("roles") String roles,
                                              @RequestHeader("email") String authorEmail,
                                              @RequestParam("deptId") Long deptId) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (deptId == null || deptId <= 0) {
                logger.warn("Invalid department ID: {}", deptId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid department ID"));
            }

            //  delete department
            boolean isDeleted = deptCommandService.deleteDepartment(deptId, authorEmail);

            //  response
            if (isDeleted) {
                logger.info("Department deleted successfully with ID: {}", deptId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Department deleted successfully."));
            } else {
                logger.error("Department deletion failed for ID: {}", deptId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Department deletion failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
