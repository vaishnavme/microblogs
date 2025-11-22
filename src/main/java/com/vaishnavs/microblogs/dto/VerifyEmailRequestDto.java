package com.vaishnavs.microblogs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailRequestDto {
  @NotBlank(message = "OTP is required")
  private String otp;
}
