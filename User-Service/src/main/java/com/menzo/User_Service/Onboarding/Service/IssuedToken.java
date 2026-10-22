package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.FeignClient.Dto.IdentityAuthData;

import java.util.List;

public record IssuedToken(IdentityAuthData data, List<String> setCookies) {
}
