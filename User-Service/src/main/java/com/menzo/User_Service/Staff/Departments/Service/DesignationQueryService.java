package com.menzo.User_Service.Staff.Departments.Service;

import com.menzo.User_Service.Staff.Departments.Repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesignationQueryService {

    @Autowired
    private DesignationRepository designationRepo;
}
