package com.menzo.User_Service.User.UserRegistration.Service;

import com.menzo.User_Service.Feign.CommunicationFeign;
import com.menzo.User_Service.User.UserProfile.Service.UserQueryService;
import com.menzo.User_Service.User.UserRegistration.Enum.UserRegistrationSource;
import com.menzo.User_Service.WishlistCart.Cart.Entity.Cart;
import com.menzo.User_Service.WishlistCart.Cart.Repository.CartRepository;
import com.menzo.User_Service.Customer.Entity.Customer;
import com.menzo.User_Service.Customer.Repository.CustomerRepository;
import com.menzo.User_Service.Exceptions.AuthFeignException;
import com.menzo.User_Service.Feign.AuthFeign;
import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.Credentials.Service.CredentialsService;
import com.menzo.User_Service.User.UserProfile.Dto.UserDto;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Enum.UserTypes;
import com.menzo.User_Service.User.UserProfile.Repository.UserRepository;
import com.menzo.User_Service.User.UserRegistration.Dto.OAuthUserDto;
import com.menzo.User_Service.User.UserRegistration.Dto.RegNewUser;
import com.menzo.User_Service.User.UserRegistration.Dto.TokenMinimalDto;
import com.menzo.User_Service.WishlistCart.Wishlist.Entity.Wishlist;
import com.menzo.User_Service.WishlistCart.Wishlist.Repository.WishlistRepository;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationService.class);

    @Autowired
    private AuthFeign authFeign;

    @Autowired
    private CommunicationFeign communicationFeign;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private WishlistRepository wishlistRepo;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserQueryService userQueryService;


    /*
     *
     *   Add new user
     *   From user sign in form
     *
     */
    public boolean addNewUser(RegNewUser newUser) {
        try {
            if (userRepo.existsByEmail(newUser.getEmail())) {
                throw new IllegalArgumentException("Email is already in use.");
            }

            //  age validation - must be at least 5+ years old
            LocalDate minDate = LocalDate.now()
                    .minusYears(5);

            if (newUser.getDateOfBirth().isAfter(minDate)) {
                throw new IllegalArgumentException("Age is below 5 years. Please enter correct date of birth.");
            }

            //  password validation
            if (!newUser.getPassword().trim().equals(newUser.getConfirmPassword().trim())) {
                throw new IllegalArgumentException("Passwords don't match.");
            }

            //  check user email already exists
            if (userQueryService.checkUserEmailExists(new EmailDto(newUser.getEmail()))) {
                throw new IllegalArgumentException("User email already exists.");
            }

            //  creating new user entity
            logger.info("New user registration: {}", newUser.getEmail());

            String encodedPassword = credentialsService.encodePassword(newUser.getPassword());
            User user = User.builder()
                    .firstName(newUser.getFirstName())
                    .lastName(newUser.getLastName())
                    .email(newUser.getEmail())
                    .phoneNumber(newUser.getPhoneNumber())
                    .gender(newUser.getGender())
                    .userType(UserTypes.CUSTOMER)
                    .dateOfBirth(newUser.getDateOfBirth())
                    .password(encodedPassword)
                    .userRegistrationSource(UserRegistrationSource.USER_SIGN_IN)
                    .build();
            User savedUser = userRepo.save(user);
            if (savedUser.getUserId() == null) {
                throw new RuntimeException("Failed to save user");
            }
            logger.info("User registered: {}", user.getEmail());

            //  returning boolean value after sending OTP to user email
            return Boolean.TRUE.equals(communicationFeign
                    .sendUserOtp(new EmailDto(savedUser.getEmail()))
                    .getBody());

        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw e;
        }
    }


    /*
     *
     *   New User Registration (customer)
     *
     */
//    public Cookie registerNewUser(RegNewUser newUser) {
//        //  creating new user entity
//        User savedUser = saveNewUser(newUser);
//
//        //  creating new customer entity
//        Customer newCustomer = saveNewCustomer(savedUser);
//
//        //  creating new customer cart entity
//        createNewCustomerCart(newCustomer);
//
//        //  creating new customer wishlist entity
//        createNewCustomerWishlist(newCustomer);
//
//        //  creating response with JWT cookie
//        TokenMinimalDto jwtToken = null;
//        try {
//            jwtToken = authFeign.generateToken(new EmailDto(savedUser.getEmail()));
//        } catch (AuthFeignException ex) {
//            logger.error("Feign error while JWT token: status = {}, message = {}", ex.getStatus(), ex.getMessage());
//            throw new RuntimeException("Identity service failed while creating JWT token", ex);
//        }
//        if (jwtToken == null || jwtToken.getToken() == null) {
//            logger.error("JWT token is null");
//            throw new RuntimeException("JWT token is null");
//        }
//        return createCookie(jwtToken.getToken());
//    }


    //   Create new customer
    private Customer saveNewCustomer(User newUser) {
        Customer newCustomer = Customer.builder()
                .user(newUser)
                .build();
        return customerRepo.save(newCustomer);
    }


    //   Create new customer cart
    private void createNewCustomerCart(Customer newCustomer) {
        Cart newCustomerCart = Cart.builder()
                .customer(newCustomer)
                .build();
        cartRepo.save(newCustomerCart);
    }


    //   Create new customer wishlist
    private void createNewCustomerWishlist(Customer newCustomer) {
        Wishlist newCustomerWishlist = Wishlist.builder()
                .customer(newCustomer)
                .build();
        wishlistRepo.save(newCustomerWishlist);
    }


    //  Create JWT cookie
    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(10 * 24 * 60 * 60);
        return cookie;
    }


    /*
     *
     *   Get Google OAuth user details
     *   For the user data got from the Google server
     *
     */
    public UserDto saveGoogleOAuthUser(OAuthUserDto googleUser) {
        if (googleUser == null || googleUser.getEmail() == null) {
            logger.error("Google OAuth user or email is null");
            throw new IllegalArgumentException("Invalid Google OAuth user data");
        }
        try {
            return userRepo.findByEmail(googleUser.getEmail())
                    .map(user -> {
                        logger.info("User already exists with email: {}", googleUser.getEmail());
                        return new UserDto(user);
                    })
                    .orElseGet(() -> {
                        User newUser = new User(googleUser);
                        newUser.setUserType(UserTypes.CUSTOMER);
                        User savedUser = userRepo.save(newUser);
                        logger.info("New Google OAuth user saved: {}", savedUser.getEmail());
                        return new UserDto(savedUser);
                    });
        } catch (DataAccessException e) {
            logger.error("Database error while saving/fetching Google OAuth user: {}", googleUser.getEmail(), e);
            throw new RuntimeException("Database error while processing OAuth login", e);
        } catch (Exception e) {
            logger.error("Unexpected error during Google OAuth user save: {}", googleUser.getEmail(), e);
            throw new RuntimeException("Unexpected error during OAuth login", e);
        }
    }
}
