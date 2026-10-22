package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.Cache.DirectedTo;
import com.menzo.User_Service.Global.Cache.Service.CacheService;
import com.menzo.User_Service.Global.Config.SignupProperties;
//import com.menzo.User_Service.Global.Dto.Response.SignupResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.SignupResponse;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.DuplicateUserException;
import com.menzo.User_Service.Global.Exception.RequestValidationException;
//import com.menzo.User_Service.Global.Feign.CommunicationFeign;
import com.menzo.User_Service.Global.Util.MaskingUtil;
import com.menzo.User_Service.Global.FeignClient.Dto.SendOtpResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.SignupResponse;
import com.menzo.User_Service.Onboarding.Enum.UserRegistrationSource;
import com.menzo.User_Service.User.Customer.WishlistCart.Cart.Repository.CartRepository;
import com.menzo.User_Service.User.Customer.Repository.CustomerRepository;
import com.menzo.User_Service.User.Profile.Credentials.Service.CredentialsService;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Enum.UserTypes;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import com.menzo.User_Service.Onboarding.Dto.RegNewUser;
import com.menzo.User_Service.User.Customer.WishlistCart.Wishlist.Repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationService.class);

    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}\\p{M}][\\p{L}\\p{M}' .-]*$");
    private static final int BCRYPT_MAX_BYTES = 72;

    private final UserRepository userRepo;
    private final CustomerRepository customerRepo;
    private final CartRepository cartRepo;
    private final WishlistRepository wishlistRepo;
    private final CredentialsService credentialsService;
    private final CacheService cacheService;
    private final PasswordEncoder passwordEncoder;
    private final InputSanitizer sanitizer;
    private final FileStorageService fileStorageService;
    private final OtpGatewayService otpGatewayService;
    private final SignupProperties signupProperties;
    private final RedirectUrlService redirectUrlService;


    public SignupResponse register(RegNewUser request) {
        SanitizedSignup data = validateAndSanitize(request);
        String masked = MaskingUtil.maskEmail(data.email());

        assertNotAlreadyRegistered(data);

        String profileUrl = fileStorageService.storeProfilePicture(request.getProfilePic());
        User saved = persistUser(request, data, profileUrl);
        logger.info("User created (unverified), userId={}, email={}", saved.getUserId(), masked);

        storeRedirectBestEffort(data);

        SendOtpResponse otp = otpGatewayService.sendSignupOtp(saved.getEmail(), saved.getFirstName());

        logger.info("Signup completed, OTP sent, userId={}", saved.getUserId());
        return new SignupResponse(data.email(), otp.expiresInSeconds(), otp.resendAfterSeconds());
    }



    // ------------------------------------------------------------------ validation / sanitisation

    private SanitizedSignup validateAndSanitize(RegNewUser request) {
        Map<String, String> errors = new LinkedHashMap<>();

        String firstName = sanitizer.cleanText(request.getFirstName());
        String lastName = sanitizer.cleanText(request.getLastName());
        if (!NAME_PATTERN.matcher(firstName).matches()) {
            errors.put("firstName", "First name contains invalid characters.");
        }
        if (!NAME_PATTERN.matcher(lastName).matches()) {
            errors.put("lastName", "Last name contains invalid characters.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.put("confirmPassword", "Passwords do not match.");
        }
        if (request.getPassword().getBytes(StandardCharsets.UTF_8).length > BCRYPT_MAX_BYTES) {
            errors.put("password", "Password is too long (maximum 72 bytes).");
        }

        String redirectUrl = sanitizer.cleanText(request.getRedirectUrl());
        if (redirectUrl == null || redirectUrl.isEmpty()) {
            redirectUrl = signupProperties.defaultRedirectUrl();
        } else if (!sanitizer.isAllowedRedirect(redirectUrl)) {
            errors.put("redirectUrl", "Redirect URL is not allowed.");
        }

        if (!errors.isEmpty()) {
            logger.warn("Signup rejected by business validation, fields={}", errors.keySet());
            throw new RequestValidationException(errors);
        }
        return new SanitizedSignup(firstName, lastName,
                sanitizer.normalizeEmail(request.getEmail()),
                sanitizer.normalizePhone(request.getPhoneNumber()),
                redirectUrl);
    }

    private void assertNotAlreadyRegistered(SanitizedSignup data) {
        Optional<User> existing = userRepo.findByEmailIgnoreCase(data.email());
        if (existing.isPresent()) {
            if (existing.get().isVerified()) {
                logger.warn("Signup rejected, email already registered: {}", MaskingUtil.maskEmail(data.email()));
                throw new DuplicateUserException(ErrorCode.EMAIL_ALREADY_REGISTERED,
                        "An account with this email already exists. Please sign in.");
            }
            logger.warn("Signup rejected, account pending verification: {}", MaskingUtil.maskEmail(data.email()));
            throw new DuplicateUserException(ErrorCode.ACCOUNT_PENDING_VERIFICATION,
                    "This email is registered but not verified yet. Please verify it, or request a new code.");
        }
//        if (userRepo.existsByPhoneNumber(data.phone())) {
//            logger.warn("Signup rejected, phone number already registered");
//            throw new DuplicateUserException(ErrorCode.PHONE_ALREADY_REGISTERED,
//                    "An account with this phone number already exists.");
//        }
    }

    private void storeRedirectBestEffort(SanitizedSignup data) {
        try {
            redirectUrlService.save(data.email(), data.redirectUrl(), DirectedTo.SIGN_UP);
        } catch (RuntimeException ex) {
            logger.warn("Could not store post-signup redirect for {}, default will be used", MaskingUtil.maskEmail(data.email()), ex);
        }
    }

    // ------------------------------------------------------------------ persistence

    private User persistUser(RegNewUser request, SanitizedSignup data, String profileUrl) {
        User user = new User();
        user.setFirstName(data.firstName());
        user.setLastName(data.lastName());
        user.setEmail(data.email());
        user.setPhoneNumber(data.phone());
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProfileUrl(profileUrl);
        user.setUserType(UserTypes.CUSTOMER);                          // TODO: match your enum constant
        user.setUserRegistrationSource(UserRegistrationSource.USER_SIGN_IN);  // TODO: match your enum constant
        user.setActive(true);
        user.setVerified(false);

        try {
            return userRepo.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {                 // unique-constraint race
            fileStorageService.deleteQuietly(profileUrl);
            logger.warn("Concurrent duplicate signup for {}", MaskingUtil.maskEmail(data.email()));
            throw new DuplicateUserException(ErrorCode.USER_ALREADY_EXISTS,
                    "An account with these details already exists.");
        } catch (RuntimeException ex) {
            fileStorageService.deleteQuietly(profileUrl);              // don't leave orphaned files behind
            throw ex;
        }
    }

    private record SanitizedSignup(String firstName, String lastName, String email, String phone, String redirectUrl) {
    }






















///    ---------------------------------------------------------------------
///    ---------------------------------------------------------------------
///    ---------------------------------------------------------------------


    /*
     *
     *   Add new user
     *   From user sign in form
     *
     */
//    public boolean addNewUser(RegNewUser newUser) {
//        try {
//            //  ------- sanitize data -------
//            //  check user email already exists
//            if (credentialsService.checkUserEmailExists(new EmailDto(newUser.getEmail()))) {
//                throw new IllegalArgumentException("Email is already in use.");
//            }
//
//            //  age validation - must be at least 5+ years old
//            LocalDate minDate = LocalDate.now()
//                    .minusYears(5);
//
//            if (newUser.getDateOfBirth().isAfter(minDate)) {
//                throw new IllegalArgumentException("Age is below 5 years. Please enter correct date of birth.");
//            }
//
//            //  password validation
//            if (!newUser.getPassword().trim().equals(newUser.getConfirmPassword().trim())) {
//                throw new IllegalArgumentException("Passwords don't match.");
//            }
//
//            //  ------- save data -------
//            //  creating new user entity
//            logger.info("New user registration: {}", newUser.getEmail());
//
//            String encodedPassword = credentialsService.encodePassword(newUser.getPassword());
//            User user = User.builder()
//                    .firstName(newUser.getFirstName())
//                    .lastName(newUser.getLastName())
//                    .email(newUser.getEmail())
//                    .phoneNumber(newUser.getPhoneNumber())
//                    .gender(newUser.getGender())
//                    .userType(UserTypes.CUSTOMER)
//                    .dateOfBirth(newUser.getDateOfBirth())
//                    .password(encodedPassword)
//                    .userRegistrationSource(UserRegistrationSource.USER_SIGN_IN)
//                    .build();
//            User savedUser = userRepo.save(user);
//
//            if (savedUser.getUserId() == null) {
//                throw new RuntimeException("Failed to save user");
//            }
//            logger.info("User registered: {}", user.getEmail());
//
//            //  caching redirect url
//            cacheService.cacheRedirectUrl(
//                    newUser.getRedirectUrl(),
//                    newUser.getEmail(),
//                    DirectedTo.SIGN_IN
//            );
//
//            //  ------- sending OTP -------
//            //  returning boolean value after sending OTP to user email
//            ResponseEntity<?> otpSendResponse = communicationFeign
//                    .sendUserSignInOtp(new EmailDto(savedUser.getEmail()));
//
//            if (otpSendResponse == null || otpSendResponse.getBody() == null) {
//                throw new RuntimeException("OTP sending failed to user email: " + savedUser.getEmail());
//            }
//
//            return Boolean.TRUE.equals(((Map<?, ?>) otpSendResponse.getBody()).get("otpSent"));
//
//        } catch (Exception e) {
//            logger.error("Error saving user: {}", e.getMessage(), e);
//            throw e;
//        }
//    }


    //  confirm user sign in
//    public void confirmSignIn() {
//        //  check verified = true
//        //  create Customer, Cart, Wishlist
//        //  return JWT cookie and redirect
//    }

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


//    //   Create new customer
//    private Customer saveNewCustomer(User newUser) {
//        Customer newCustomer = Customer.builder()
//                .user(newUser)
//                .build();
//        return customerRepo.save(newCustomer);
//    }
//
//
//    //   Create new customer cart
//    private void createNewCustomerCart(Customer newCustomer) {
//        Cart newCustomerCart = Cart.builder()
//                .customer(newCustomer)
//                .build();
//        cartRepo.save(newCustomerCart);
//    }
//
//
//    //   Create new customer wishlist
//    private void createNewCustomerWishlist(Customer newCustomer) {
//        Wishlist newCustomerWishlist = Wishlist.builder()
//                .customer(newCustomer)
//                .build();
//        wishlistRepo.save(newCustomerWishlist);
//    }
//
//
//    //  Create JWT cookie
//    private Cookie createCookie(String token) {
//        Cookie cookie = new Cookie("JWT", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(false);
//        cookie.setPath("/");
//        cookie.setMaxAge(10 * 24 * 60 * 60);
//        return cookie;
//    }


    /*
     *
     *   Get Google OAuth user details
     *   For the user data got from the Google server
     *
     */
//    public UserDto saveGoogleOAuthUser(OAuthUserDto googleUser) {
//        if (googleUser == null || googleUser.getEmail() == null) {
//            logger.error("Google OAuth user or email is null");
//            throw new IllegalArgumentException("Invalid Google OAuth user data");
//        }
//        try {
//            return userRepo.findByEmail(googleUser.getEmail())
//                    .map(user -> {
//                        logger.info("User already exists with email: {}", googleUser.getEmail());
//                        return new UserDto(user);
//                    })
//                    .orElseGet(() -> {
//                        User newUser = new User(googleUser);
//                        newUser.setUserType(UserTypes.CUSTOMER);
//                        User savedUser = userRepo.save(newUser);
//                        logger.info("New Google OAuth user saved: {}", savedUser.getEmail());
//                        return new UserDto(savedUser);
//                    });
//        } catch (DataAccessException e) {
//            logger.error("Database error while saving/fetching Google OAuth user: {}", googleUser.getEmail(), e);
//            throw new RuntimeException("Database error while processing OAuth login", e);
//        } catch (Exception e) {
//            logger.error("Unexpected error during Google OAuth user save: {}", googleUser.getEmail(), e);
//            throw new RuntimeException("Unexpected error during OAuth login", e);
//        }
//    }
}
