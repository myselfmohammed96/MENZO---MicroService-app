package com.menzo.Home_Service.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String indexPage(){
        return "IndexTemplates/index-page.html";
    }

}
