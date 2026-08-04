package com.menzo.Product_Service.Feign;

import com.menzo.Product_Service.GlobalComponents.Dto.EmailDto;
import com.menzo.Product_Service.GlobalComponents.Dto.StaffDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("USER-SERVICE")
public interface UserFeign {

    /*
    *
    *   Get staff ID by using staff email address
    *   ## need to create this endpoint in USER-SERVICE
    *
     */
    @PostMapping("/staff/get-staff-id")
    public StaffDto getStaffIdByStaffEmail(@RequestBody EmailDto email);

}
