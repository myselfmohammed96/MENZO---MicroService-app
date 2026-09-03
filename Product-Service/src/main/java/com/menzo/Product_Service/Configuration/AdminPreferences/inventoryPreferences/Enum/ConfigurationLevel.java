package com.menzo.Product_Service.Configuration.AdminPreferences.inventoryPreferences.Enum;

public enum ConfigurationLevel {

    CRITICAL,           // App/business cannot work correctly
    REQUIRED,           // Must be configured for a feature
    IMPORTANT,          // Strongly recommended
    OPTIONAL,           // Can safely remain unset
    INFORMATIONAL       // Doesn't affect operation

}
