package com.menzo.Product_Service.Discount.Repository;

import com.menzo.Product_Service.Discount.Entity.Discount;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID>, JpaSpecificationExecutor<Discount> {

    boolean existsByDiscountCode(@NotBlank String discountCode);


    //  status - SCHEDULED -> ACTIVE
    @Modifying
    @Query("""
            UPDATE Discount d
                SET d.discountStatus = "ACTIVE"
                WHERE d.discountStatus = "SCHEDULED"
                    AND d.startAt <= :now
            """)
    int activateScheduled(@Param("now") LocalDateTime now);


    //  status - ACTIVE | INACTIVE | PAUSED -> EXPIRED
    @Modifying
    @Query("""
            UPDATE Discount d
                SET d.discountStatus = "EXPIRED"
                WHERE d.discountStatus IN ("ACTIVE", "INACTIVE", "PAUSED")
                    AND d.endAt < :now
            """)
    int expireActive(@Param("now") LocalDateTime now);


    //  status - PAUSED -> SCHEDULED | ACTIVE
    @Modifying
    @Query("""
            UPDATE Discount d
                SET d.discountStatus = 
                    CASE
                        WHEN d.startAt <= :now THEN "ACTIVE"
                        ELSE "SCHEDULED"
                    END
                WHERE d.discountStatus = "PAUSED"
                    AND d.resumeAt IS NOT NULL 
                    AND d.resumeAt <= :now
            """)
    int resumePaused(@Param("now") LocalDateTime now);
}
