package com.vaishnavs.microblogs.users;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

  private final UserEntity user;

  public UserPrincipal(UserEntity user) {
    this.user = user;
  }

  public String getId() {
    return user.getId();
  }

  public UserEntity getUserEntity() {
    return user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isEnabled() {
    return user.getIsActive();
  }

  public String getEmail() {
    return user.getEmail();
  }
}
