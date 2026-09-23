package com.example.GameService.controllers;

import com.example.GameService.config.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;

public abstract class BaseController {
    protected AuthenticatedUser authPlayer(
            Authentication authentication
    ) {
        return (AuthenticatedUser) authentication.getPrincipal();
    }

}
