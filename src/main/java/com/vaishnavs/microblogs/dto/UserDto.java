package com.vaishnavs.microblogs.dto;

import com.vaishnavs.microblogs.model.UserEntity;

public record UserDto(
    String id,
    String email,
    String firstName,
    String lastName,
    String username,
    Boolean isActive) {
  public static UserDto fromEntity(UserEntity entity) {
    return new UserDto(
        entity.getId(),
        entity.getEmail(),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getUsername(),
        entity.getIsActive());
  }
}
