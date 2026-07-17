package com.menzo.Identity_Service.JWT.Service;

import com.menzo.Identity_Service.Dto.EmailDto;
import com.menzo.Identity_Service.JWT.Dto.TokenDto;
import com.menzo.Identity_Service.Dto.User;
import com.menzo.Identity_Service.Dto.UserStatusDto;
import com.menzo.Identity_Service.JWT.Entity.Token;
import com.menzo.Identity_Service.Feign.UserFeign;
import com.menzo.Identity_Service.JWT.Repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtUtilityService jwtUtility;



    /*
    *
    *   Generate JWT for user email
    *   after revoking previous tokens
    *
    */
    public String generateToken(String userEmail) {
        User user = userFeign.getUserbyUserEmail(new EmailDto(userEmail));
        String token = jwtUtility.generateToken(userEmail);

        boolean oldTokensRevoked = revokeAllTokensByUser(user);
        if (oldTokensRevoked) {
            tokenRepository.save(new Token(token, false, user.getId()));
        }
        logger.info("JWT generated for user ID: {}", user.getId());
        return token;
    }



    /*
    *
    *   Revoke old JWTs
    *
    */
    private boolean revokeAllTokensByUser(User user) {
        List<Token> tokenList = tokenRepository.findAllTokensByUser(user.getId());

        if (tokenList.isEmpty()) {
            logger.info("No active tokens found for user ID: {}", user.getId());
            return true;
        } else {
            tokenList.stream()
                    .filter(t -> !t.isLoggedOut())
                    .forEach(t -> t.setLoggedOut(true));
            tokenRepository.saveAll(tokenList);
        }
        logger.info("Revoked {} tokens for user ID: {}", tokenList.size(), user.getId());
        return true;
    }



    /*
    *
    *   Get Token entity by token
    *
    */
    public TokenDto getByToken(String token) {
//        logger.info("Fetching token entity");
        Token tokenInDB = tokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found: " + token));
        UserStatusDto userDto = userFeign.getUserByUserId(tokenInDB.getUserId());
        return new TokenDto(
                tokenInDB.getTokenId(),
                tokenInDB.getToken(),
                tokenInDB.isLoggedOut(),
                tokenInDB.getUserId(),
                userDto.isActive()
        );
    }

}
