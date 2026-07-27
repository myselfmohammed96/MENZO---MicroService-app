package com.menzo.User_Service.Staff.StaffProfile.Controller;

import com.menzo.User_Service.Staff.StaffProfile.Service.StaffQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
public class StaffQueryRestController {

    @Autowired
    private StaffQueryService staffQueryService;

}
