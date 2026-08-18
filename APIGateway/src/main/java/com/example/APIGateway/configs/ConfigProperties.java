package com.example.APIGateway.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties()
@Component
public class ConfigProperties {
    private String secretKey;
    private Integer expRefresh;
    private Integer expAccess;
    private String iss;
    private String aud;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getExpRefresh() {
        return expRefresh;
    }

    public void setExpRefresh(Integer expRefresh) {
        this.expRefresh = expRefresh;
    }

    public Integer getExpAcces() {
        return expAccess;
    }

    public void setExpAcces(Integer expAcces) {
        this.expAccess = expAcces;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }
}
