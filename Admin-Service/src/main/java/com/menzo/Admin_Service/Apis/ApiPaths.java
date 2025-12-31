package com.menzo.Admin_Service.Apis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ApiPaths {
    private Discount discount;
    private Product product;
    private Navigate navigate;

    @Getter
    @Setter
    public static class Discount {
        private Command command;
        private MappingApi mappingApi;
        private Query query;
        private EnumApi enumApi;


        @Getter
        @Setter
        public static class Command {
            private String post;
            private String patch;
            private String delete;
        }

        @Getter
        @Setter
        public static class MappingApi {
            private String post;
        }

        @Getter
        @Setter
        public static class Query {
            private String listing;
            private String summary;
            private String mappedContent;
        }

        @Getter
        @Setter
        public static class EnumApi {
            private String discountLevel;
            private String discountType;
            private String capType;
            private String formStatus;
            private String summaryStatus;
        }
    }

    @Getter
    @Setter
    public static class Product {
        private Command command;

        @Getter
        @Setter
        public static class Command {
            private String post;
            private String patch;
            private String delete;
        }
    }

    @Getter
    @Setter
    public static class Navigate {
        private Page page;

        @Getter
        @Setter
        public static class Page {
            private String index;
            private String dashboard;
            private String categories;
            private String variations;
            private String products;
            private String discounts;
            private String coupons;
            private String orders;
            private String returns;
            private String sales;
            private String users;
            private String blockedUsers;
            private String subscriptions;
            private String newsLetters;
            private String contactRequests;
            private String sentMessages;
            private String banners;
            private String aboutUs;
        }
    }
}
