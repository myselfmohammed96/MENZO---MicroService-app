package com.menzo.Communication_Service.Email.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {

    @Email
    @NotBlank
    private String to;

    private String fromName;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

}
