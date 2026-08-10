package com.menzo.User_Service.Wishlist.Service;

import com.menzo.User_Service.Cart.Entity.CartItem;
import com.menzo.User_Service.Cart.Service.CartCommandService;
import com.menzo.User_Service.Exceptions.UnauthorizedAccessException;
import com.menzo.User_Service.Feign.ProductFeign;
import com.menzo.User_Service.GlobalComponents.CustomAnnotations.Annotations.EnableWishlistItemFilter;
import com.menzo.User_Service.GlobalComponents.Enum.Response;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Service.UserQueryService;
import com.menzo.User_Service.Wishlist.Entity.Wishlist;
import com.menzo.User_Service.Wishlist.Entity.WishlistItem;
import com.menzo.User_Service.Wishlist.Repository.WishlistItemRepository;
import com.menzo.User_Service.Wishlist.Repository.WishlistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class WishlistCommandService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistCommandService.class);

    @Autowired
    private WishlistItemRepository wishlistItemRepo;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private ProductFeign productFeign;

    @Autowired
    private CartCommandService cartCommandService;


    /*
     *
     *   Add new product-item in wishlist
     *
     */
    @Transactional
    public Response addNewWishlistItem(String userEmail,
                                       UUID productItemId) {
        //  fetching customer wishlist
        User user = userQueryService.getUserEntityByEmail(userEmail);
        Wishlist wishlist = user.getCustomer().getWishlist();

        //  fetching product-item by SKU
        String sku = productFeign.getSkuByItemId(productItemId).getBody();
        if (sku == null) {
            throw new EntityNotFoundException("Product-item SKU not found for product-item ID: " + productItemId);
        }

        //  fetching wishlist-item if exist
        Optional<WishlistItem> wishlistItemOpt = wishlistItemRepo.findByWishlist_WishlistIdAndProductItemSku(wishlist.getWishlistId(), sku);

        if (wishlistItemOpt.isPresent()) {
            WishlistItem wishlistItem = wishlistItemOpt.get();

            //  return if product-item already exist in wishlist
            if (!wishlistItem.isDeleted() && !wishlistItem.isMovedToCart()) {
                return Response.ALREADY_EXISTS;
            }

            //  undo isDeleted & isMovedToCart and refresh addedAt
            if (wishlistItem.isDeleted()) {
                wishlistItem.setDeleted(false);
            }
            if (wishlistItem.isMovedToCart()) {
                wishlistItem.setMovedToCart(false);
            }
            wishlistItem.setAddedAt(LocalDateTime.now());
            wishlistItemRepo.save(wishlistItem);

            return Response.RESTORED;
        }

        //  adding new wishlist-item
        WishlistItem newWishlistItem = WishlistItem.builder()
                .wishlist(wishlist)
                .productItemId(productItemId)
                .productItemSku(sku)
                .addedAt(LocalDateTime.now())
                .build();
        try {
            wishlistItemRepo.save(newWishlistItem);
            return Response.CREATED;
        } catch (Exception e) {
            logger.error("Failed to save new item to wishlist: ", e);
            return Response.FAILED;
        }
    }


    /*
     *
     *   Move wishlist-item to cart
     *
     */
    public boolean moveWishlistItemToCart(String userEmail,
                                          UUID wishlistItemId) {
        //  fetching user
        User user = userQueryService.getUserEntityByEmail(userEmail);

        //  fetching wishlist-item
        WishlistItem wishlistItem = wishlistItemRepo.findById(wishlistItemId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist-item not found with ID: " + wishlistItemId));

        //  checking wishlist-item belongs to authenticated user
        if (!Objects.equals(user.getUserId(),
                wishlistItem.getWishlist().getCustomer().getUser().getUserId())) {
            throw new UnauthorizedAccessException("Access denied: wishlist-item does not belong to the authenticated user.");
        }

        //  move item to cart
        boolean movedToCart = cartCommandService.moveWishlistItemToCart(
                user,
                wishlistItem.getProductItemId(),
                wishlistItem.getProductItemSku()
        );

        //  mark wishlist-item as moved
        if (movedToCart) {
            wishlistItem.setMovedToCart(true);
            wishlistItem.setMovedToCartAt(LocalDateTime.now());
            wishlistItemRepo.save(wishlistItem);
            return true;
        } else {
            return false;
        }
    }


    /*
     *
     *   Move cart-item to wishlist
     *   Used by CartCommandService
     *
     */
    public boolean moveCartItemToWishlist(User user,
                                          UUID productItemId,
                                          String productItemSku) {
        //  fetching user wishlist
        Wishlist userWishlist = user.getCustomer().getWishlist();

        //  checking if product-item already exists in wishlist
        Optional<WishlistItem> wishlistItemOpt = wishlistItemRepo.findByWishlist_WishlistIdAndProductItemSku(
                userWishlist.getWishlistId(),
                productItemSku
        );

        //  reuse wishlist-item if already present
        if (wishlistItemOpt.isPresent()) {
            WishlistItem wishlistItem = wishlistItemOpt.get();

            if (wishlistItem.isMovedToCart()) {
                wishlistItem.setMovedToCart(false);
                wishlistItemRepo.save(wishlistItem);
            }
            return true;
        }

        //  create new wishlist-item if not already present
        WishlistItem newWishlistItem = WishlistItem.builder()
                .wishlist(userWishlist)
                .productItemId(productItemId)
                .productItemSku(productItemSku)
                .build();
        wishlistItemRepo.save(newWishlistItem);
        return true;
    }


    /*
     *
     *   Delete wishlist item (soft delete)
     *
     */
    public boolean deleteWishlistItem(String userEmail,
                                      UUID wishlistItemId) {
        //  fetching user
        User user = userQueryService.getUserEntityByEmail(userEmail);

        //  fetching wishlist-item
        WishlistItem wishlistItem = wishlistItemRepo.findById(wishlistItemId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist-item not found with ID: " + wishlistItemId));

        //  checking wishlist-item belongs to authenticated user
        if (Objects.equals(user.getUserId(),
                wishlistItem.getWishlist().getCustomer().getUser().getUserId())) {
            throw new UnauthorizedAccessException("Access denied: wishlist-item does not belong to the authenticated user.");
        }

        //  deleting wishlist-item
        wishlistItem.setDeleted(true);
        wishlistItem.setDeletedAt(LocalDateTime.now());
        return true;
    }

}

