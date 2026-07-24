package com.menzo.User_Service.DataSeeder.SystemAccessSeeder;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "system-access")
@Data
public class SystemAccessProperties {

    private Map<String, Module> modules;

    @Data
    public static class Module {
        private String name;
        private String description;
        private List<Action> actions;
    }

    @Data
    @AllArgsConstructor
    public static class Action {
        private String code;
        private String name;
        private String description;
    }


    /*
     *
     *   Module details extraction
     *
     */
    public List<ModuleDetails> getModuleDetails() {
        return modules.entrySet().stream()
                .map(entry -> new ModuleDetails(
                        entry.getKey(),
                        entry.getValue().getName(),
                        entry.getValue().getDescription()
                )).toList();
    }


    @Data
    @AllArgsConstructor
    public static class ModuleDetails {
        private String code;
        private String name;
        private String description;
    }


    /*
    *
    *   Task details extraction
    *   String (Map.key) -> represents the module code
    *   List<Action> (Map.value) -> represents the List of tasks/actions present under the module
    *
    */
    public Map<String, List<Action>> getTaskDetails() {
        return modules.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getActions())
                );
    }
}
