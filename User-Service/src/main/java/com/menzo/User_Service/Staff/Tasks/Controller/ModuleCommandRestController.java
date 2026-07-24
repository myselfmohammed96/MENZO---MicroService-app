package com.menzo.User_Service.Staff.Tasks.Controller;

import com.menzo.User_Service.Staff.Tasks.Service.ModuleCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/module")
public class ModuleCommandRestController {

    @Autowired
    private ModuleCommandService moduleCommandService;

    private static Logger logger = LoggerFactory.getLogger(ModuleCommandRestController.class);


    /*
    *
    *   Update module active status
    *
    */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateModuleActiveStatus(@RequestHeader("roles") String roles,
                                                      @RequestParam("id") Long moduleId,
                                                      @RequestParam("active") boolean isActive) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (moduleId == null || moduleId <= 0) {
                logger.warn("Invalid module ID: {}", moduleId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid module ID"));
            }

            //  update active status
            boolean updatedActive = moduleCommandService.updateModuleActiveStatus(moduleId, isActive);

            //  response
            if (isActive == updatedActive) {
                logger.info("Active status for module {}, updated successfully", moduleId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Module active status updated successfully"));
            } else {
                logger.error("Active status update failed for module ID: {}", moduleId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Module active status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
    *
    *   Update display order
    *
    */
//    @PutMapping("/update-display-order")
//    public ResponseEntity<?> updateModuleDisplayOrder() {}
}
