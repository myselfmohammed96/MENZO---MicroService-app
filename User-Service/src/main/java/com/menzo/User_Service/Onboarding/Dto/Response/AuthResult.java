package com.menzo.User_Service.Onboarding.Dto.Response;

import java.util.List;

public record AuthResult(AuthResponse body, List<String> setCookies, boolean authenticated) {
}
