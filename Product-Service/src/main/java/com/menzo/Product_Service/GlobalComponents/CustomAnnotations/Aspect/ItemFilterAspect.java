package com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Aspect;

import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableItemFilter;
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
public class ItemFilterAspect {

    private final EntityManager entityManager;

    @Around("@annotation(EnableItemFilter)")
    public Object applyFilter(ProceedingJoinPoint joinPoint,
                              EnableItemFilter enableItemFilter) throws Throwable {

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter("itemFilter")
                .setParameter("isActive", enableItemFilter.isActive())
                .setParameter("isDeleted", enableItemFilter.isDeleted());

        try {
            return joinPoint.proceed();
        } finally {
            session.disableFilter("itemFilter");
        }
    }
}
