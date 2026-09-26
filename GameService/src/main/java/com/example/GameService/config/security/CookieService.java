package com.example.GameService.config.security;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CookieService {

    public String readTokenFromCookie (Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue).orElse(null);
    }
}
