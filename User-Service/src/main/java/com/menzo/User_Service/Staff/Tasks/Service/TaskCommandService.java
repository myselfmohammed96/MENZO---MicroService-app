package com.menzo.User_Service.Staff.Tasks.Service;

import com.menzo.User_Service.Staff.Tasks.Entity.Task;
import com.menzo.User_Service.Staff.Tasks.Repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskCommandService {

    @Autowired
    private TaskRepository taskRepo;


    /*
    *
    *   Update task active status
    *   Task identified by task ID
    *
    */
    public boolean updateTaskActiveStatus(UUID taskId, boolean isActive) {

        //  fetching task by ID
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        //  updating task active status
        task.setActive(isActive);
        return taskRepo.save(task).isActive();
    }
}
