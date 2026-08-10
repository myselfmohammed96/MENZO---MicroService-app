package com.menzo.User_Service.Staff.StaffProfile.Service;

import com.menzo.User_Service.Staff.StaffProfile.Entity.Staff;
import com.menzo.User_Service.Staff.StaffProfile.Repository.StaffRepository;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StaffQueryService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StaffRepository staffRepo;


    /*
     *
     *   Get depart head staff by ID
     *
     */
    public Staff getHeadStaffById(UUID departmentHeadId) {
        Staff staff = staffRepo.findById(departmentHeadId)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with staff ID: " + departmentHeadId));

        //  make sure the staff is the head staff
        return staff;
    }


    /*
     *
     *   Get staff by email
     *
     */
    public Staff getStaffByEmail(String staffEmail) {
        User user = userRepo.findByEmail(staffEmail)
                .orElseThrow(() -> new EntityNotFoundException("Staff user not found with email: " + staffEmail));

        return staffRepo.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with user ID: " + user.getUserId()));
    }
}
