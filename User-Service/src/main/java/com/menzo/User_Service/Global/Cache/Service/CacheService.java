package com.menzo.User_Service.Global.Cache.Service;

import com.menzo.User_Service.Global.Cache.DirectedTo;
import com.menzo.User_Service.Global.Cache.Entity.RedirectUrlTemp;
import com.menzo.User_Service.Global.Cache.Repository.RedirectUrlTempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private RedirectUrlTempRepository urlCacheRepo;


    /*
     *
     *   Cache redirect url
     *
     */
    public void cacheRedirectUrl(String redirectUrl,
                                 String userEmail,
                                 DirectedTo directedTo) {
        urlCacheRepo.save(RedirectUrlTemp.builder()
                .redirectUrl(redirectUrl)
                .userEmail(userEmail)
                .directedTo(directedTo)
                .build()
        );
    }

}
