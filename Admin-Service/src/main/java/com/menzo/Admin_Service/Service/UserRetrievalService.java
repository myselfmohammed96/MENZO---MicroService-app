package com.menzo.Admin_Service.Service;

import com.menzo.Admin_Service.Dto.UserListingDto;
import com.menzo.Admin_Service.Feign.UserServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserRetrievalService {

    @Autowired
    private UserServiceFeign userFeign;

    public Page<UserListingDto> getUsersListingWithPagination(Integer page, Integer size) {
        return userFeign.getUsersListWithPagination(page, size);
    }
}
