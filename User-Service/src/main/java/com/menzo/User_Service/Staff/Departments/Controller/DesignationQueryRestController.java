package com.menzo.User_Service.Staff.Departments.Controller;

import com.menzo.User_Service.Staff.Departments.Service.DesignationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designation")
public class DesignationQueryRestController {

    @Autowired
    private DesignationQueryService designationQueryService;

}
