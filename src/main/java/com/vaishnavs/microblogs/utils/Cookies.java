package com.vaishnavs.microblogs.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class Cookies {
    public static Cookie createCookieToken(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Lax");
        cookie.setAttribute("SameSite", "Lax");
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        return cookie;
    }

    public static Cookie clearCookieToken() {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set true when using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete cookie
        return cookie;
    }
}
