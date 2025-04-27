package com.menzo.Email_Service.Service;

import com.menzo.Email_Service.Util.EmailSender;
import com.menzo.Email_Service.Util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    EmailSender emailSender;

    @Autowired
    OtpUtil otpUtil;

    public void otpVerification(String userEmail) {
        emailSender.sendOtpEmail(userEmail, otpUtil.generateOtp());
    }
}
