package com.menzo.Admin_Service.Feign;

import com.menzo.Admin_Service.Dto.UserListingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8081")
public interface UserServiceFeign {

    @GetMapping("/user/users-listing")
    public Page<UserListingDto> getUsersListWithPagination(@RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "15") Integer size);
}
