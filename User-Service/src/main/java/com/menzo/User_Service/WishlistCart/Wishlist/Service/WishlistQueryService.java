package com.menzo.User_Service.WishlistCart.Wishlist.Service;

import com.menzo.User_Service.Feign.ProductFeign;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Service.UserQueryService;
import com.menzo.User_Service.WishlistCart.Wishlist.Dto.WishlistDto;
import com.menzo.User_Service.WishlistCart.Wishlist.Entity.Wishlist;
import com.menzo.User_Service.WishlistCart.Wishlist.Entity.WishlistItem;
import com.menzo.User_Service.WishlistCart.Wishlist.Repository.WishlistItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistQueryService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistQueryService.class);

    @Autowired
    private WishlistItemRepository wishlistItemRepo;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private ProductFeign productFeign;


    /*
     *
     *   Get all wishlist-items with pagination
     *   Wishlist-items sorted by createdAt (latest first)
     *
     */
    public Page<?> getWishlistItemsWithPagination(String userEmail, Integer page, Integer size) {

        //  fetching user wishlist
        User user = userQueryService.getUserEntityByEmail(userEmail);
        Wishlist wishlist = user.getCustomer().getWishlist();

        //  getting wishlist page
        Pageable sortedPageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC,"addedAt")
        );
        Page<WishlistItem> wishlistItems = wishlistItemRepo.findAllByWishlist_WishlistId(
                wishlist.getWishlistId(),
                sortedPageable
        );

        //  getting wishlist page data
        List<WishlistItem> pageContent = wishlistItems.getContent();
        List<UUID> wishlistItemIds = pageContent.stream()
                .map(WishlistItem::getProductItemId)
                .toList();

        List<WishlistDto> wishlistItemData = productFeign.getWishlistItemsData(wishlistItemIds).getBody();
        if (wishlistItemData == null) {
            throw new RuntimeException();
        }

        return new PageImpl<>(wishlistItemData, sortedPageable, wishlistItems.getTotalElements());
    }

}
