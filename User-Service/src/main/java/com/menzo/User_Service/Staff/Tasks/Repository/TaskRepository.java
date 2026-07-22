package com.menzo.User_Service.Staff.Tasks.Repository;

import com.menzo.User_Service.Staff.Tasks.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
