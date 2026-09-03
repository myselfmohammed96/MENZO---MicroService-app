package com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableVariationFilter {

    /*
     *
     *   For default values as parameters:
     *   @EnableVariationFilter
     *
     *   For custom values as parameters:
     *   @EnableVariationFilter(applyActive = false, applyDeleted = true, isDeleted = false)
     *   @EnableVariationFilter(isActive = false, isDeleted = true)
     *   @EnableVariationFilter(isActive = false)
     *
     */
    boolean applyActive() default true;
    boolean isActive() default true;

    boolean applyDeleted() default true;
    boolean isDeleted() default false;

}
