package com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Aspect;

import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableProductFilter;
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
public class ProductFilterAspect {

    private final EntityManager entityManager;

    @Around("@annotation(EnableProductFilter)")
    public Object applyFilter(ProceedingJoinPoint joinPoint,
                              EnableProductFilter enableProductFilter) throws Throwable {

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter("productFilter")
                .setParameter("isActive", enableProductFilter.isActive())
                .setParameter("isDeleted", enableProductFilter.isDeleted());

        try {
            return joinPoint.proceed();
        } finally {
            session.disableFilter("productFilter");
        }
    }
}
