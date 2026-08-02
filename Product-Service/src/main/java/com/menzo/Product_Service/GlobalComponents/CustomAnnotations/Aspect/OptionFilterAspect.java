package com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Aspect;

import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableOptionFilter;
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
public class OptionFilterAspect {

    private final EntityManager entityManager;

    @Around("@annotation(EnableOptionFilter)")
    public Object applyFilter(ProceedingJoinPoint joinPoint,
                              EnableOptionFilter enableOptionFilter) throws Throwable {

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter("optionFilter")
                .setParameter("isActive", enableOptionFilter.isActive())
                .setParameter("isDeleted", enableOptionFilter.isDeleted());

        try {
            return joinPoint.proceed();
        } finally {
            session.disableFilter("optionFilter");
        }
    }
}
