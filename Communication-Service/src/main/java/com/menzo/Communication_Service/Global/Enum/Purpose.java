package com.menzo.Communication_Service.Global.Enum;

public enum Purpose {

    USER_SIGN_IN("User Sign-In"),
    ADD_USER_PASSWORD("Add User Password");

    private final String displayName;

    Purpose(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
