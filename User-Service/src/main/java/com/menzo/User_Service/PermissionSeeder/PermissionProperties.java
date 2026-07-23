package com.menzo.User_Service.PermissionSeeder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "permissions")
@Component
public class PermissionProperties {

//    private Map<String, List<String>> modules;
//
//    public Map<String, List<String>> getModules() {
//        return modules;
//    }
//
//    public void setModules(Map<String, List<String>> modules) {
//        this.modules = modules;
//    }
//
//    public String toString() {
//        return getModules().toString();
//    }

}
