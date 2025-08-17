package com.menzo.Admin_Service.Controller;

import com.menzo.Admin_Service.Dto.UserListingDto;
import com.menzo.Admin_Service.Service.UserRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserRetrievalService userRetrievalService;

    @GetMapping("/users")
    public String adminUserManagement(@RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "15") Integer size,
                                      Model model) {
        Page<UserListingDto> usersListPage = userRetrievalService.getUsersListingWithPagination(page, size);
        model.addAttribute("userList", usersListPage.getContent());
        return "Users/user-management";
    }

    @GetMapping("/user")
    public String adminUserDetailsById(@RequestParam("id") Long userId,
                                       Model model) {
        model.addAttribute("userId", userId);
        return "Users/user-details";
    }
}
