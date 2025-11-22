package com.vaishnavs.microblogs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaishnavs.microblogs.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
  UserEntity findByEmail(String email);
}
