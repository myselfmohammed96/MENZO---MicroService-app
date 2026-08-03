package com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableProductFilter {

    /*
     *
     *   For default values as parameters:
     *   @EnableProductFilter
     *
     *   For custom values as parameters:
     *   @EnableProductFilter(isActive = false, isDeleted = true)
     *   @EnableProductFilter(isActive = false)
     *
     */
    boolean isActive() default true;

    boolean isDeleted() default false;

}
