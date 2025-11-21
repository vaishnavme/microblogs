package com.vaishnavs.microblogs.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vaishnavs.microblogs.users.UserEntity;
import com.vaishnavs.microblogs.users.UserPrincipal;
import com.vaishnavs.microblogs.users.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final UserService userService;

  public JWTAuthFilter(JWTService jwtService, UserService userService) {
    this.jwtService = jwtService;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Cookie[] cookies = request.getCookies();

    try {
      if (cookies != null && cookies.length > 0) {
        Cookie tokenCookie = Arrays.stream(cookies)
            .filter(c -> c.getName().equals("token"))
            .findFirst()
            .orElse(null);

        if (tokenCookie.getValue() != null) {
          try {

            String id = jwtService.validatedJWTToken(tokenCookie.getValue());
            UserEntity user = userService.getBy(id);
            UserPrincipal principal = new UserPrincipal(user);
            System.out.println("Authenticated user: " + user.getEmail());
            System.out.println("principal user: " + principal.getEmail());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
                null,
                principal.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
          } catch (Exception e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            //
          }
        }
      }

    } catch (Exception e) {
      //
    }

    filterChain.doFilter(request, response);
  }
}
