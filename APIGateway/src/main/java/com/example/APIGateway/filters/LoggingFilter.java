package com.example.APIGateway.filters;

import jakarta.annotation.PostConstruct;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @PostConstruct
    public void init() {
        System.out.println(">>> LoggingFilter loaded");
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        var request = exchange.getRequest();

        System.out.println("============== GATEWAY ==============");
        System.out.println("Method : " + request.getMethod());
        System.out.println("Path   : " + request.getURI().getPath());
        System.out.println("URI    : " + request.getURI());
        System.out.println("====================================");

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}