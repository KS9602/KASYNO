package com.example.AuthService.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties()
@Component
public class ConfigProperties {
    private String secretKey;
    private Integer expRefresh;
    private Integer expAccess;
    private String iss;
    private String aud;
}
