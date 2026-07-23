//package com.menzo.User_Service.PermissionSeeder;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class PermissionSyncService {
//
//    private final PermissionProperties properties;
//
//    public PermissionSyncService(PermissionProperties properties) {
//        this.properties = properties;
//    }
//
////    public void sync() {
//////        System.out.println("Look at here... \n");
//////        System.out.println(properties);
////        properties.getModules().forEach((moduke, actions) -> {
////            for(String actionCode : actions) {
////                System.out.println(actionCode);
//////                Action action = actionRepository
//////                        .findByCode(actionCode)
//////                        .orElse(new Action());
//////
//////                action.setCode(actionCode);
//////                action.setModule(module);
//////                action.setActive(true);
//////
//////                actionRepository.save(action);
////            }
////        });
////    }
//
//}
