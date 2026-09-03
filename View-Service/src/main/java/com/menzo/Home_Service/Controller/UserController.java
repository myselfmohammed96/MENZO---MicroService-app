package com.menzo.Home_Service.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {


    /*
     *
     *   Get user profile page
     *
     */
    @GetMapping("/user-profile")
    public String getUserProfilePage() {
        return "user-template/user-profile.html";
    }


    /*
     *
     *   Get user address page
     *
     */
    @GetMapping("/user-address")
    public String getUserAddressPage() {
        return "user-template/user-address.html";
    }


    /*
     *
     *   Get user cart page
     *
     */
    @GetMapping("/user-cart")
    public String getUserCartPage() {
        return "user-template/user-cart.html";
    }


    /*
     *
     *   Get user wishlist page
     *
     */
    @GetMapping("/user-wishlist")
    public String getUserWishlistPage() {
        return "user-template/user-wishlist.html";
    }


    /*
     *
     *   Get user coupon page
     *
     */
    @GetMapping("/user-coupon")
    public String getUserCouponPage() {
        return "user-template/user-coupon.html";
    }


    /*
     *
     *   Get user order page
     *
     */
    @GetMapping("/user-order")
    public String getUserOrderPage() {
        return "user-template/user-order.html";
    }


    /*
     *
     *   Get user wallet page
     *
     */
    @GetMapping("/user-wallet")
    public String getUserWalletPage() {
        return "user-template/user-wallet.html";
    }

}
