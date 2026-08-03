package com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableItemFilter {

    /*
     *
     *   For default values as parameters:
     *   @EnableItemFilter
     *
     *   For custom values as parameters:
     *   @EnableItemFilter(isActive = false, isDeleted = true)
     *   @EnableItemFilter(isActive = false)
     *
     */
    boolean isActive() default true;

    boolean isDeleted() default false;

 }
