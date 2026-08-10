package com.menzo.User_Service.Staff.Departments.Controller;

import com.menzo.User_Service.Staff.Departments.Service.DesignationCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/designation")
public class DesignationCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(DesignationCommandRestController.class);

    @Autowired
    private DesignationCommandService designationCommandService;


    /*
    *
    *   Update designation active status
    *
    */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateDesignationActiveStatus(@RequestHeader("roles") String roles,
                                                           @RequestParam("id") UUID designationId,
                                                           @RequestParam("active") boolean isActive) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (designationId == null) {
                logger.warn("Invalid designation ID: {}", designationId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid designation ID"));
            }

            //  update active status
            boolean updatedActive = designationCommandService.updateDesignationActiveStatus(designationId, isActive);

            //  response
            if (isActive == updatedActive) {
                logger.info("Active status for designation {}, updated successfully", designationId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Designation active status updated successfully"));
            } else {
                logger.error("Active status update failed for designation ID: {}", designationId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Department active status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }



    //  level add & update



    //  add and edit and delete
    //  additional designations

}
