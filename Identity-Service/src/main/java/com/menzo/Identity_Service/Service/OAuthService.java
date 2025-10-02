package com.menzo.Identity_Service.Service;

import com.menzo.Identity_Service.Dto.OAuthUserDto;
import com.menzo.Identity_Service.Dto.User;
import com.menzo.Identity_Service.Feign.UserFeign;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.token.url}")
    private String tokenUrl;

    @Value("${google.userinfo.url}")
    private String userinfoUrl;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.scopes.openid}")
    private String openidScope;

    @Value("${google.scopes.profile}")
    private String profileScope;

    @Value("${google.scopes.email}")
    private String emailScope;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private AuthService authService;

    //    OAuth config

    public Cookie processGrantCode(String code) {
        String accessToken = getOauthAccessTokenGoogle(code);
        OAuthUserDto googleUser = getProfileDetailsGoogle(accessToken);

//        System.out.println(accessToken + "\n" + googleUser.getEmail() + "\n" + googleUser.getUserName() + "\n" + googleUser.getProfileUrl());
        User user = userFeign.googleOAuthUser(googleUser);

        String token = authService.generateToken(user.getEmail());
        logger.info("JWT token created for google oauth user: {}", user.getEmail());

        return authService.createCookie(token);
    }

    private String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", profileScope);
        params.add("scope", emailScope);
        params.add("scope", openidScope);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = tokenUrl;
        String response = restTemplate.postForObject(url, requestEntity, String.class);

        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        return jsonObject.get("access_token").toString().replace("\"", "");
    }

    private OAuthUserDto getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = userinfoUrl;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);

        OAuthUserDto user = new OAuthUserDto(
                jsonObject.get("email").toString().replace("\"", ""),
                jsonObject.get("name").toString().replace("\"", ""),
                jsonObject.get("picture").toString().replace("\"", "")
        );

        return user;
    }
}
