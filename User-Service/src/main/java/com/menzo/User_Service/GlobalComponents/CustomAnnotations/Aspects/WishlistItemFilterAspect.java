package com.menzo.User_Service.GlobalComponents.CustomAnnotations.Aspects;

import com.menzo.User_Service.GlobalComponents.CustomAnnotations.Annotations.EnableWishlistItemFilter;
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
public class WishlistItemFilterAspect {

    private final EntityManager entityManager;

    @Around("@annotation(EnableWishlistItemFilter)")
    public Object applyFilter(ProceedingJoinPoint joinPoint,
                              EnableWishlistItemFilter enableWishlistItemFilter) throws Throwable {

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter("wishlistItemFilter")
                .setParameter("applyMovedToCart", enableWishlistItemFilter.applyMovedToCart())
                .setParameter("movedToCart", enableWishlistItemFilter.movedToCart())
                .setParameter("applyDeleted", enableWishlistItemFilter.applyDeleted())
                .setParameter("isDeleted", enableWishlistItemFilter.isDeleted());

        try {
            return joinPoint.proceed();
        } finally {
            session.disableFilter("wishlistItemFilter");
        }
    }
}
