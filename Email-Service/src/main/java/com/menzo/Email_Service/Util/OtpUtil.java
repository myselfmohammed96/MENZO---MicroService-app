package com.menzo.Email_Service.Util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {

    public String generateOtp(){
        StringBuilder s = new StringBuilder();
        for (int i=0; i<4; i++){
            s.append(new Random().nextInt(10));
        }
        return s.toString();
    }

}
