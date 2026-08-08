package com.menzo.User_Service.GlobalComponents.CustomAnnotations.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableWishlistItemFilter {

    /*
     *
     *   For default values as parameters:
     *   @EnableWishlistItemFilter
     *
     *   For custom values as parameters:
     *   @EnableWishlistItemFilter(applyMovedToCart = false, applyDeleted = true, isDeleted = false)
     *   @EnableWishlistItemFilter(movedToCart = true, isDeleted = true)
     *   @EnableWishlistItemFilter(movedToCart = true)
     *
     */
    boolean applyMovedToCart() default true;
    boolean movedToCart() default false;

    boolean applyDeleted() default true;
    boolean isDeleted() default false;

}
