package com.example.APIGateway.filters;

import com.example.APIGateway.services.JwtService;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

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

        if(allowedPaths.contains(path)) {
            return chain.filter(exchange);
        }

        String tokenType = path.equals("/api/auth/refresh") ? "refresh_token" : "access_token";   // wrzucic w configi
        HttpCookie cookie = exchange.getRequest().getCookies().getFirst(tokenType);
        if(cookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

        }
        String token = cookie.getValue();
        if (token.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        if (Boolean.TRUE.equals(jwtService.isTokenExpired(token))) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add(
                    "X-Auth-Error",
                    "TOKEN_EXPIRED"
            );
            return exchange.getResponse().setComplete();
        }

        String username = jwtService.extractUsername(token);
        if(username == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
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