package com.menzo.Product_Service.Discount.Enum;

public enum OperationalStatus {

//  Lifecycle
    SCHEDULED,
    ACTIVE,
    PAUSED,
    EXPIRED,

//  Manual/terminal
    INACTIVE,
    CANCELLED,

//  Coupon specific
    REDEEMED,
    USED,
    LIMITED

}
