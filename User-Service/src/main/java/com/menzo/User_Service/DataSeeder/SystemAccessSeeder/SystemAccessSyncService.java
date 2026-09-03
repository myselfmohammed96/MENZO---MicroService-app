package com.menzo.User_Service.DataSeeder.SystemAccessSeeder;

import com.menzo.User_Service.Staff.Tasks.Entity.Module;
import com.menzo.User_Service.Staff.Tasks.Entity.Task;
import com.menzo.User_Service.Staff.Tasks.Repository.ModuleRepository;
import com.menzo.User_Service.Staff.Tasks.Repository.TaskRepository;
import com.menzo.User_Service.Staff.Tasks.Service.ModuleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SystemAccessSyncService {

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private ModuleQueryService moduleQueryService;

    private final SystemAccessProperties systemAccessProps;

    public SystemAccessSyncService(SystemAccessProperties systemAccessProps) {
        this.systemAccessProps = systemAccessProps;
    }


    /*
     *
     *   Sync module details to modules table
     *
     */
    public void syncModules() {
        systemAccessProps.getModuleDetails().forEach(module -> {
            Module newModule = moduleRepo.findByModuleCode(module.getCode())
                    .orElseGet(() -> Module.builder()
                            .moduleCode(module.getCode())
                            .moduleName(module.getName())
                            .description(module.getDescription())
                            .build()
                    );
            moduleRepo.save(newModule);
        });
    }


    /*
     *
     *   Sync task details to tasks table
     *
     */
    public void syncTasks() {
        systemAccessProps.getTaskDetails().entrySet().forEach(taskDetail ->
                taskDetail.getValue().forEach(action -> {
                    Task newTask = taskRepo.findByTaskCode(action.getCode())
                            .orElseGet(() -> Task.builder()
                                    .taskCode(action.getCode())
                                    .taskName(action.getName())
                                    .module(moduleQueryService.getModuleByCode(taskDetail.getKey()))
                                    .description(action.getDescription())
                                    .build()
                            );
                    taskRepo.save(newTask);
                }));
    }
}
