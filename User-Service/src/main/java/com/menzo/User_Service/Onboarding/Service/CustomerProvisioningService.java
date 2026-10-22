package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.User.Customer.Entity.Customer;
import com.menzo.User_Service.User.Customer.Repository.CustomerRepository;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Customer.WishlistCart.Cart.Entity.Cart;
import com.menzo.User_Service.User.Customer.WishlistCart.Cart.Repository.CartRepository;
import com.menzo.User_Service.User.Customer.WishlistCart.Wishlist.Entity.Wishlist;
import com.menzo.User_Service.User.Customer.WishlistCart.Wishlist.Repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerProvisioningService {

    private final CustomerRepository customerRepo;
    private final CartRepository cartRepo;
    private final WishlistRepository wishlistRepo;

    @Transactional
    public Customer provision(User user) {
        Customer customer = customerRepo.findByUser(user).orElseGet(() -> {
            log.info("Creating customer profile for userId={}", user.getUserId());
            return customerRepo.save(Customer.builder()
                    .user(user)
                    .loyaltyPoints(0)
                    .totalOrders(0)
                    .totalSpent(BigDecimal.ZERO)
                    .build());
        });
        if (!cartRepo.existsByCustomer(customer)) {
            cartRepo.save(Cart.builder().customer(customer).build());
        }
        if (!wishlistRepo.existsByCustomer(customer)) {
            wishlistRepo.save(Wishlist.builder().customer(customer).build());
        }
        return customer;
    }
}
