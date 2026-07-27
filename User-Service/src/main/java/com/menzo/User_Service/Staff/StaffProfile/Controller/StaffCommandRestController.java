package com.menzo.User_Service.Staff.StaffProfile.Controller;

import com.menzo.User_Service.Staff.StaffProfile.Service.StaffCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
public class StaffCommandRestController {

    @Autowired
    private StaffCommandService staffCommandService;

//    public ResponseEntity<?> sk(){}

    /*
    * ***** ADD *****
    *   admin adds staff with invitation
    *   staff accepting invitation
    *
    *
    * ***** EDIT *****
    *
    *
    * ***** DELETE *****
    *
    */
}
