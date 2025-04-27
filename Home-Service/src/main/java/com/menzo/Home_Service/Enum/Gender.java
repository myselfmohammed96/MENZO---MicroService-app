package com.menzo.Home_Service.Enum;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    public String getDisplayName(){
        return switch(this){
            case MALE -> "Male";
            case FEMALE -> "Female";
            case OTHER -> "Other";
        };
    }
}
