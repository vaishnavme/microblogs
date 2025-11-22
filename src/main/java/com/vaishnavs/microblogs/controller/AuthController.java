package com.vaishnavs.microblogs.controller;

import com.vaishnavs.microblogs.config.JWTService;
import com.vaishnavs.microblogs.dto.LoginRequestDto;
import com.vaishnavs.microblogs.dto.SignupRequestDto;
import com.vaishnavs.microblogs.dto.UserDto;
import com.vaishnavs.microblogs.dto.VerifyEmailRequestDto;
import com.vaishnavs.microblogs.model.UserEntity;
import com.vaishnavs.microblogs.principal.UserPrincipal;
import com.vaishnavs.microblogs.service.UserService;
import com.vaishnavs.microblogs.utils.Cookies;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Setter
  @Autowired
  private UserService userService;

  @Setter
  @Autowired
  private JWTService jwtService;

  @PostMapping("/signup")
  public ResponseEntity<UserDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto,
      HttpServletResponse httpServletResponse) {
    UserEntity user = userService.create(signupRequestDto.getEmail(), signupRequestDto.getPassword());

    String token = jwtService.generateToken(user.getId());
    Cookie cookie = Cookies.createCookieToken(token);
    httpServletResponse.addCookie(cookie);

    return ResponseEntity.ok(UserDto.fromEntity(user));
  }

  @PostMapping("/verify")
  public ResponseEntity<UserDto> verifyEmail(
      @Valid @RequestBody VerifyEmailRequestDto verifyEmailRequestDto,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    UserDto user = userService.verifyEmail(verifyEmailRequestDto.getOtp(), userPrincipal.getEmail());

    return ResponseEntity.ok(user);
  }

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(
      @Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
    UserEntity user = userService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());

    String token = jwtService.generateToken(user.getId());
    Cookie cookie = Cookies.createCookieToken(token);
    httpServletResponse.addCookie(cookie);

    return ResponseEntity.ok(UserDto.fromEntity(user));
  }
}
