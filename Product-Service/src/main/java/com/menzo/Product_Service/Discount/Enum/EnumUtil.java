package com.menzo.Product_Service.Discount.Enum;

import org.springframework.stereotype.Component;

@Component
public class EnumUtil {

    public static <E extends Enum<E>> E toEnum(Class<E> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid value for " + enumClass.getSimpleName() + ": " + value
            );
        }
    }
    
}
