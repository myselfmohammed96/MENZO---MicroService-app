package com.menzo.User_Service.GlobalComponents.CustomAnnotations.Aspects;

import com.menzo.User_Service.GlobalComponents.CustomAnnotations.Annotations.EnableCartItemFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CartItemFilterAspect {

    private final EntityManager entityManager;

    @Around("@annotation(EnableCartItemFilter)")
    public Object applyFilter(ProceedingJoinPoint joinPoint,
                              EnableCartItemFilter enableCartItemFilter) throws Throwable {

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter("cartItemFilter")
                .setParameter("applySelected", enableCartItemFilter.applySelected())
                .setParameter("isSelected", enableCartItemFilter.isSelected())
                .setParameter("applyOrdered", enableCartItemFilter.applyOrdered())
                .setParameter("isOrdered", enableCartItemFilter.isOrdered())
                .setParameter("applyMovedToWishlist", enableCartItemFilter.applyMovedToWishlist())
                .setParameter("movedToWishlist", enableCartItemFilter.movedToWishlist())
                .setParameter("applyDeleted", enableCartItemFilter.applyDeleted())
                .setParameter("isDeleted", enableCartItemFilter.isDeleted());

        try {
            return joinPoint.proceed();
        } finally {
            session.disableFilter("cartItemFilter");
        }
    }
}
