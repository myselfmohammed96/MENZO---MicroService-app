package com.menzo.User_Service.DataSeeder;

import com.menzo.User_Service.DataSeeder.DesignationSeeder.DesignationSyncService;
import com.menzo.User_Service.DataSeeder.SystemAccessSeeder.SystemAccessSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeederRunner implements ApplicationRunner {

    @Autowired
    private SystemAccessSyncService systemAccessSync;

    @Autowired
    private DesignationSyncService designationSync;


    /*
    *
    *   Run seeder sync on application startup
    *
    */
    @Override
    public void run(ApplicationArguments args) {
        //  modules seeder sync
        systemAccessSync.syncModules();

        //  tasks seeder sync
        systemAccessSync.syncTasks();

        //  designation seeder sync
        designationSync.syncDesignations();
    }
}