package com.menzo.Home_Service.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {


    /*
     *
     *   Get check out page
     *
     */
    @GetMapping("/check-out")
    public String getCheckOutPage() {
        return "checkout-template/check-out.html";
    }
}
