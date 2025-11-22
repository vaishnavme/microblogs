package com.vaishnavs.microblogs.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vaishnavs.microblogs.exception.UnauthorizedException;
import com.vaishnavs.microblogs.model.UserEntity;
import com.vaishnavs.microblogs.principal.UserPrincipal;
import com.vaishnavs.microblogs.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

  @Setter
  @Autowired
  private JWTService jwtService;

  @Setter
  @Autowired
  private UserService userService;

  private static final List<String> PUBLIC_PATHS = List.of(
      "/auth/login",
      "/auth/signup");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    // Remove /api prefix if present
    String cleanPath = path.replaceFirst("^/api", "");
    return PUBLIC_PATHS.stream().anyMatch(cleanPath::startsWith);
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

        if (tokenCookie == null || tokenCookie.getValue() == null) {
          throw new UnauthorizedException("Invalid access!");
        }

        String id = jwtService.validatedJWTToken(tokenCookie.getValue());
        UserEntity user = userService.getBy(id);

        if (user == null) {
          throw new UnauthorizedException("Invalid access!");
        }

        UserPrincipal principal = new UserPrincipal(user);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
            null,
            principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

    } catch (UnauthorizedException ex) {
      throw ex;
    } catch (Exception e) {
      throw new ServletException("Something went wrong while authenticating user!", e);
    }

    filterChain.doFilter(request, response);
  }
}
