package com.menzo.Product_Service.Modules.Discount.Repo;

import com.menzo.Product_Service.Modules.Discount.Entity.Discount;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, UUID> {

    boolean existsByDiscountCode(@NotBlank String discountCode);


    //  status - SCHEDULED -> ACTIVE
    @Modifying
    @Query("""
            UPDATE Discount d
                SET d.discountStatus = "ACTIVE"
                WHERE d.discountStatus = "SCHEDULED"
                    AND d.startAt <= :now
            """)
    void activateScheduled(@Param("now") LocalDateTime now);


    //  status - ACTIVE | INACTIVE | PAUSED -> EXPIRED
    @Modifying
    @Query("""
            UPDATE Discount d
                SET d.discountStatus = "EXPIRED"
                WHERE d.discountStatus IN ("ACTIVE", "INACTIVE", "PAUSED")
                    AND d.endAt < :now
            """)
    void expireActive(@Param("now") LocalDateTime now);


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
    void resumePaused(@Param("now") LocalDateTime now);
}
