package com.vaishnavs.microblogs.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailRequest {
    @NotBlank(message = "OTP is required")
    private String otp;
}
