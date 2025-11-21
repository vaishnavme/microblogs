package com.vaishnavs.microblogs.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaishnavs.microblogs.utils.OTP;

@Service
public class UserService {

  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final OTP otpUtils;

  public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, OTP otpUtils) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
    this.otpUtils = otpUtils;
  }

  public UserEntity create(String email, String password) {
    if (userRepo.findByEmail(email) != null) {
      throw new RuntimeException("Email already in use!");
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
      throw new RuntimeException("User not found by email!");
    }

    if (!user.getVerificationCode().equals(otp)) {
      throw new RuntimeException("Invalid OTP!");
    }

    user.setVerificationCode(null);
    user.setIsActive(true);

    userRepo.save(user);
    return UserDto.fromEntity(user);
  }

  public UserEntity getBy(String id) {
    return userRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found by id"));
  }
}
