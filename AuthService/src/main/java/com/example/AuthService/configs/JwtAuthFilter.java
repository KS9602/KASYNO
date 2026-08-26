package com.example.AuthService.configs;


import com.example.AuthService.services.CookieService;
import com.example.AuthService.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final CookieService cookieService;
    private final JwtService jwtService;
    private final List<String> allowedPaths = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/logout"
    );

    public JwtAuthFilter(
            @Lazy UserDetailsService userDetailsService,
            JwtService jwtService,
            CookieService cookieService)
    {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        System.out.println("path: " + path);
        if(allowedPaths.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("NOT Allowed path: " + path);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cookie [] cookies = request.getCookies();
        System.out.println("cookies: " + Arrays.toString(cookies));
        String tokenType = path.equals("/auth/refresh") ? "refresh_token" : "access_token";   // wrzucic w configi
        System.out.println("tokenType: " + tokenType);

        if(cookies == null) {
            System.out.println("cookies is null");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing cookie");
            return;

        }
        String token = cookieService.readTokenFromCookie(cookies, tokenType);
        System.out.println("token: " + token);

        if (token == null || token.isEmpty()) {
            System.out.println("token is null");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }
        if(Boolean.TRUE.equals(jwtService.isTokenExpired(token))) {
            System.out.println("token is expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("X-Auth-Error","TOKEN_EXPIRED");
            return;
        }

        String username = jwtService.extractUsername(token);
        System.out.println("username: " + username);


        if (username != null && authentication == null) {
            System.out.println("username is not null");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!jwtService.validateToken(token, userDetails)) {
                System.out.println("token is not valid");
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid token"
                );
                return;
            }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

        }
        System.out.println("tokenxxxxxxxxxxx: " + token);
        filterChain.doFilter(request, response);
    }




}