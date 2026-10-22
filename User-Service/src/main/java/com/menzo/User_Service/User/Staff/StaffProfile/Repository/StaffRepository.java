package com.menzo.User_Service.User.Staff.StaffProfile.Repository;

import com.menzo.User_Service.User.Staff.StaffProfile.Entity.Staff;
import com.menzo.User_Service.User.Profile.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {

    public Optional<Staff> findByUser(User user);

}
