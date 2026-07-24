package com.menzo.User_Service.Staff.Tasks.Controller;

import com.menzo.User_Service.Staff.Tasks.Service.TaskCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(TaskCommandRestController.class);

    @Autowired
    private TaskCommandService taskCommandService;


    /*
    *
    *   Update task active status
    *
    */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateTaskActiveStatus(@RequestHeader("roles") String roles,
                                                    @RequestParam("id") Long taskId,
                                                    @RequestParam("active") boolean isActive) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (taskId == null || taskId <= 0) {
                logger.warn("Invalid task ID: {}", taskId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid task ID"));
            }

            //  update active status
            boolean updatedActive = taskCommandService.updateTaskActiveStatus(taskId, isActive);

            //  response
            if (isActive == updatedActive) {
                logger.info("Active status for task {}, updated successfully", taskId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Task active status updated successfully"));
            } else {
                logger.error("Active status update failed for task ID: {}", taskId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Task active status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
