package com.menzo.Email_Service.Service;

import com.menzo.Email_Service.Entity.User;

import java.util.List;

public interface UserService {

    boolean saveUser(User user);

    List<User> fetchAllUser();

    User fetchUserById(Long id);

    boolean deleteUser(Long id);

    boolean updateUser(Long id, User user);
}
