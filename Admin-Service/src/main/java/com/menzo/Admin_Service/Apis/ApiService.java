package com.menzo.Admin_Service.Apis;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
public class ApiService {

    @Autowired
    private ApiPaths apiPaths;


    /// /   ********* DISCOUNT pages *********

    private String addDiscountApi;

    private Map<String, String> discountFormApis;
    private Map<String, String> discountListingApis;
    private Map<String, String> discountSummaryApis;

    @PostConstruct
    void discountPagesApiMapsInit() {
        var discount = apiPaths.getDiscount();

        addDiscountApi = discount.getCommand().getPost();

        discountFormApis = Map.of(
                "getDiscountLevel", discount.getEnumApi().getDiscountLevel(),
                "getDiscountType", discount.getEnumApi().getDiscountType(),
                "getDiscountStatus", discount.getEnumApi().getFormStatus(),
                "getCapType", discount.getEnumApi().getCapType(),
                "successRedirect", discount.getQuery().getSummary()
        );
        discountListingApis = Map.of(
                "getDiscounts", discount.getQuery().getListing()
        );
        discountSummaryApis = Map.of(
                "getSummary", discount.getQuery().getSummary(),
                "getMappedContent", discount.getQuery().getMappedContent()
        );
    }

    /// /   ********* NAVIGATION APIs *********

    private String logoutApi;
    private Map<String, String> sideNavApis;

    @PostConstruct
    void navApisInit() {
        var pages = apiPaths.getNavigate().getPage();

        sideNavApis = Map.ofEntries(
                Map.entry("index", pages.getIndex()),
                Map.entry("dashBoard", pages.getDashboard()),
                Map.entry("categories", pages.getCategories()),
                Map.entry("variations", pages.getVariations()),
                Map.entry("products", pages.getProducts()),
                Map.entry("discounts", pages.getDiscounts()),
                Map.entry("coupons", pages.getCoupons()),
                Map.entry("orders", pages.getOrders()),
                Map.entry("returns", pages.getReturns()),
                Map.entry("sales", pages.getSales()),
                Map.entry("users", pages.getUsers()),
                Map.entry("blockedUsers", pages.getBlockedUsers()),
                Map.entry("subscriptions", pages.getSubscriptions()),
                Map.entry("newsLetters", pages.getNewsLetters()),
                Map.entry("contactRequests", pages.getContactRequests()),
                Map.entry("sentMessages", pages.getSentMessages()),
                Map.entry("banners", pages.getBanners()),
                Map.entry("aboutUs", pages.getAboutUs())
        );
    }

    /// /   ********* PRODUCT APIs *********





}
