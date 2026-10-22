package com.menzo.User_Service.Global.Cache.Repository;

import com.menzo.User_Service.Global.Cache.DirectedTo;
import com.menzo.User_Service.Global.Cache.Entity.RedirectUrlTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RedirectUrlTempRepository extends JpaRepository<RedirectUrlTemp, UUID> {

    Optional<RedirectUrlTemp> findFirstByUserEmailAndDirectedToOrderByCreatedAtDesc(String userEmail, DirectedTo directedTo);

    @Modifying
    @Query("delete from RedirectUrlTemp r where r.userEmail = :email and r.directedTo = :directedTo")
    int deleteByEmailAndDirectedTo(@Param("email") String email, @Param("directedTo") DirectedTo directedTo);

    @Modifying
    @Query("delete from RedirectUrlTemp r where r.createdAt < :cutoff")
    int deleteOlderThan(@Param("cutoff") LocalDateTime cutoff);
}
