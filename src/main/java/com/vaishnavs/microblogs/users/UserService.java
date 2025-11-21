package com.vaishnavs.microblogs.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaishnavs.microblogs.exception.BadRequestException;
import com.vaishnavs.microblogs.exception.ResourceNotFoundException;
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
    return userRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found by id: " + id));
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
}
