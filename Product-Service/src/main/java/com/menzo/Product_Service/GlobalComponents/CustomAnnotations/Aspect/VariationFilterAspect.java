package com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Aspect;

import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableVariationFilter;
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
public class VariationFilterAspect {

    private final EntityManager entityManager;

    @Around("@annotation(EnableVariationFilter")
    public Object applyFilter(ProceedingJoinPoint joinPoint,
                              EnableVariationFilter enableVariationFilter) throws Throwable {

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter("variationFilter")
                .setParameter("isActive", enableVariationFilter.isActive())
                .setParameter("isDeleted", enableVariationFilter.isDeleted());

        try {
            return joinPoint.proceed();
        } finally {
            session.disableFilter("variationFilter");
        }
    }
}
