package com.menzo.Admin_Service.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Hello from Admin.!";
    }
}
