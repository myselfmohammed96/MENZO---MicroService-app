package com.menzo.User_Service.Global.FeignClient.Dto;

public record IssueTokenRequest(String userId, String email, String role, AuthMethod authMethod) {
}
