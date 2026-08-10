package com.menzo.User_Service.Staff.Tasks.Repository;

import com.menzo.User_Service.Staff.Tasks.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    public Optional<Task> findByTaskCode(String taskCode);

}
