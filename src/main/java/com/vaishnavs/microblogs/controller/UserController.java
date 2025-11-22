package com.vaishnavs.microblogs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaishnavs.microblogs.dto.UpdateUserRequestDto;
import com.vaishnavs.microblogs.dto.UserDto;
import com.vaishnavs.microblogs.model.UserEntity;
import com.vaishnavs.microblogs.principal.UserPrincipal;
import com.vaishnavs.microblogs.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    UserEntity user = userPrincipal.getUserEntity();

    return ResponseEntity.ok(UserDto.fromEntity(user));
  }

  @PutMapping("/update")
  public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserRequestDto updateUserRequestDto,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    String userId = userPrincipal.getUserEntity().getId();

    UserEntity user = userService.updateUser(userId, updateUserRequestDto.getFirstName(),
        updateUserRequestDto.getLastName(), updateUserRequestDto.getUsername());

    return ResponseEntity.ok(UserDto.fromEntity(user));
  }
}
