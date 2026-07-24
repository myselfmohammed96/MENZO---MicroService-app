package com.menzo.User_Service.Staff.Tasks.Repository;

import com.menzo.User_Service.Staff.Tasks.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    public Optional<Task> findByTaskCode(String taskCode);

}
