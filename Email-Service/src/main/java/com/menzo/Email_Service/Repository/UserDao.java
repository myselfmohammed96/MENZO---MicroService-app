package com.menzo.Email_Service.Repository;

import com.menzo.Email_Service.Entity.User;

import java.util.List;

public interface UserDao {

    boolean saveUser(User user);

    List<User> fetchAllUser();

    User fetchUserById(Long id);

    boolean deleteUser(Long id);

    boolean updateUser(Long id, User user);
}
