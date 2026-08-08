package com.menzo.User_Service.GlobalComponents.CustomAnnotations.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCartItemFilter {

    /*
     *
     *   For default values as parameters:
     *   @EnableCartItemFilter
     *
     *   For custom values as parameters:
     *   @EnableCartItemFilter(applyOrdered = false, applyDeleted = true, isDeleted = false)
     *   @EnableCartItemFilter(isOrdered = true, isDeleted = true)
     *   @EnableCartItemFilter(isOrdered = true)
     *
     */
    boolean applySelected() default false;
    boolean isSelected() default true;

    boolean applyOrdered() default true;
    boolean isOrdered() default false;

    boolean applyMovedToWishlist() default true;
    boolean movedToWishlist() default false;

    boolean applyDeleted() default true;
    boolean isDeleted() default false;

}
