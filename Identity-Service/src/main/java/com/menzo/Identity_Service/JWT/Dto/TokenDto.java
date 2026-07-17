package com.menzo.Identity_Service.JWT.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {

    private Long tokenId;

    private String token;

    private boolean loggedOut;

    private Long userId;

    private boolean userIsActive;

}
