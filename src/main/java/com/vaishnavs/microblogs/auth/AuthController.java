package com.vaishnavs.microblogs.auth;

import com.vaishnavs.microblogs.config.JWTService;
import com.vaishnavs.microblogs.users.UserDto;
import com.vaishnavs.microblogs.users.UserEntity;
import com.vaishnavs.microblogs.users.UserPrincipal;
import com.vaishnavs.microblogs.users.UserService;
import com.vaishnavs.microblogs.utils.Cookies;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final JWTService jwtService;

  AuthController(UserService userService, JWTService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  @PostMapping("/signup")
  public ResponseEntity<UserDto> signup(@Valid @RequestBody SignupRequest request, HttpServletResponse response) {
    UserEntity user = userService.create(request.getEmail(), request.getPassword());

    String token = jwtService.generateToken(user.getId());
    Cookie cookie = Cookies.createCookieToken(token);
    response.addCookie(cookie);

    return new ResponseEntity<>(UserDto.fromEntity(user), HttpStatus.OK);
  }

  @PostMapping("/verify")
  public ResponseEntity<UserDto> verifyEmail(
      @Valid @RequestBody VerifyEmailRequest request,
      @AuthenticationPrincipal UserPrincipal principal) {

    UserDto user = userService.verifyEmail(request.getOtp(), principal.getEmail());

    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
