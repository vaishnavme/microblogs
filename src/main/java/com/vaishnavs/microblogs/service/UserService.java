package com.vaishnavs.microblogs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaishnavs.microblogs.dto.UserDto;
import com.vaishnavs.microblogs.exception.BadRequestException;
import com.vaishnavs.microblogs.exception.ResourceNotFoundException;
import com.vaishnavs.microblogs.model.UserEntity;
import com.vaishnavs.microblogs.repository.UserRepository;
import com.vaishnavs.microblogs.utils.OTP;

import lombok.Setter;

@Service
public class UserService {

  @Setter
  @Autowired
  private UserRepository userRepo;

  @Setter
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Setter
  @Autowired
  private OTP otpUtils;

  public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, OTP otpUtils) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
    this.otpUtils = otpUtils;
  }

  public UserEntity create(String email, String password) {
    if (userRepo.findByEmail(email) != null) {
      throw new BadRequestException("Email already in use!");
    }

    UserEntity user = new UserEntity();
    user.setEmail(email);
    user.setIsActive(false);
    user.setVerificationCode(otpUtils.generateOtp(6));

    String hashedPassword = passwordEncoder.encode(password);
    user.setPassword(hashedPassword);

    userRepo.save(user);
    return user;
  }

  public UserDto verifyEmail(String otp, String email) {
    UserEntity user = userRepo.findByEmail(email);
    if (user == null) {
      throw new BadRequestException("User not found by email!");
    }

    if (!user.getVerificationCode().equals(otp)) {
      throw new BadRequestException("Invalid OTP!");
    }

    user.setVerificationCode(null);
    user.setIsActive(true);

    userRepo.save(user);
    return UserDto.fromEntity(user);
  }

  public UserEntity getBy(String id) {
    if (id == null) {
      throw new BadRequestException("User id cannot be null");
    }
    return userRepo.findById(id).orElse(null);
  }

  public UserEntity authenticate(String email, String password) {
    UserEntity user = userRepo.findByEmail(email);
    if (user == null) {
      throw new ResourceNotFoundException("User not found by email " + email);
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadRequestException("Invalid password!");
    }

    return user;
  }

  public UserEntity updateUser(String userId, String firstName, String lastName, String username) {
    UserEntity user = getBy(userId);

    if (user == null) {
      throw new ResourceNotFoundException("User not found by id " + userId);
    }

    if (firstName != null && !firstName.isBlank()) {
      user.setFirstName(firstName);
    }
    if (lastName != null && !lastName.isBlank()) {
      user.setLastName(lastName);
    }
    if (username != null && !username.isBlank()) {
      UserEntity existingUser = userRepo.findByUsername(username);
      if (existingUser != null && !existingUser.getId().equals(userId)) {
        throw new BadRequestException("Username already in use!");
      }
      user.setUsername(username);
    }

    userRepo.save(user);
    return user;
  }
}
