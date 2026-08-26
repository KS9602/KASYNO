package com.example.AuthService.services;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CookieService {

    public Cookie addTokenToCookie (String token, String type, int ttl) {
        Cookie cookie = new Cookie(type, token);
        cookie.setPath("/");        // todo zrobic dynamiczne
        cookie.setMaxAge(ttl);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public String readTokenFromCookie (Cookie[] cookies, String tokenType) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(tokenType))
                .findFirst()
                .map(Cookie::getValue).orElse(null);
    }
}
