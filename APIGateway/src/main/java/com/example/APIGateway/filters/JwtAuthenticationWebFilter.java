package com.example.APIGateway.filters;

import com.example.APIGateway.services.JwtService;
import io.netty.handler.codec.http.cookie.Cookie;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationWebFilter implements WebFilter {

    private final JwtService jwtService;
    private final List<String> allowedPaths = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/logout"
    );

    public JwtAuthenticationWebFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        System.out.println("path: " + path);

        if(allowedPaths.contains(path)) {
            System.out.println("Allowed path: " + path);
            return chain.filter(exchange);
        }
        System.out.println("Not allowed path: " + path);

        String tokenType = path.equals("/api/auth/refresh") ? "refresh_token" : "access_token";   // wrzucic w configi
        System.out.println("COOKIES: " + exchange.getRequest().getCookies());
        HttpCookie cookie = exchange.getRequest().getCookies().getFirst(tokenType);
        System.out.println("cookie: " + cookie);
        if(cookie == null) {
            System.out.println("Cookie not found: " + tokenType);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

        }
        String token = cookie.getValue();
        System.out.println("token: " + token);
        if (token.isEmpty()) {
            System.out.println("token is empty");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        System.out.println("token: " + token);
        if (Boolean.TRUE.equals(jwtService.isTokenExpired(token))) {
            System.out.println("token is expired");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add(
                    "X-Auth-Error",
                    "TOKEN_EXPIRED"
            );
            return exchange.getResponse().setComplete();
        }
        System.out.println("token: " + token);

        String username = jwtService.extractUsername(token);
        System.out.println("username: " + username);
        if(username == null) {
            System.out.println("username is null");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        System.out.println("username: " + username);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of());

        return chain.filter(exchange)
                .contextWrite(
                        ReactiveSecurityContextHolder.withAuthentication(authentication)
                );
    }
}