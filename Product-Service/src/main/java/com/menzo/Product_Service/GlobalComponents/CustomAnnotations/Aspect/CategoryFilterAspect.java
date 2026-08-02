package com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Aspect;

import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableCategoryFilter;
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
public class CategoryFilterAspect {

    private final EntityManager entityManager;

    @Around("@annotation(EnableCategoryFilter)")
    public Object applyFilter(ProceedingJoinPoint joinPoint,
                              EnableCategoryFilter enableCategoryFilter) throws Throwable {

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter("categoryFilter")
                .setParameter("isActive", enableCategoryFilter.isActive())
                .setParameter("isDeleted", enableCategoryFilter.isDeleted());

        try {
            return joinPoint.proceed();
        } finally {
            session.disableFilter("categoryFilter");
        }
    }
}
