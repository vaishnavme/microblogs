package com.vaishnavs.microblogs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaishnavs.microblogs.dto.UserDto;
import com.vaishnavs.microblogs.model.UserEntity;
import com.vaishnavs.microblogs.principal.UserPrincipal;

@RestController
@RequestMapping("/users")
public class UserController {
  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
    UserEntity user = principal.getUserEntity();

    return ResponseEntity.ok(UserDto.fromEntity(user));
  }
}
