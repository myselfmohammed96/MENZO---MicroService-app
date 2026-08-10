package com.menzo.User_Service.Staff.StaffProfile.Repository;

import com.menzo.User_Service.Staff.StaffProfile.Entity.Staff;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {

    public Optional<Staff> findByUser(User user);

}
