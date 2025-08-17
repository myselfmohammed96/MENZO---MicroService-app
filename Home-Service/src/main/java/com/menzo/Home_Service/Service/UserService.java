package com.menzo.Home_Service.Service;

import com.menzo.Home_Service.Dto.ClientSideUserDetailsDto;
import com.menzo.Home_Service.Dto.EmailDto;
import com.menzo.Home_Service.Feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserFeign userFeign;

    public ClientSideUserDetailsDto getUserDetailsForClientSide(String userEmail) {
        return userFeign.getUserDetailsForClientSide(new EmailDto(userEmail));
    }
}
