package com.elliegabel.s.player.web;

import com.elliegabel.s.http.ApiController;
import com.elliegabel.s.http.util.Validate;
import com.elliegabel.s.player.dto.auth.LoginNotification;
import com.elliegabel.s.player.domain.service.PlayerAuthService;
import com.elliegabel.s.player.dto.auth.PreLoginRequest;
import com.elliegabel.s.player.dto.auth.PreLoginResponse;
import io.avaje.http.api.*;
import jakarta.inject.Inject;

import java.util.UUID;

@Controller("/auth/{playerId}")
public class AuthController implements ApiController {

    private final PlayerAuthService service;

    @Inject
    public AuthController(PlayerAuthService service) {
        this.service = service;
    }

    /* The user wants to log on. */
    @Get("/prelogin")
    public PreLoginResponse preLogin(UUID playerId, PreLoginRequest request) {
        Validate.requireString("playerIp", request.playerIp());

        return service.preLogin(playerId, request.playerIp());
    }

    /* The user has logged onto the network. */
    @Post("/login")
    public void login(UUID playerId, LoginNotification notification) {
        Validate.requireString("playerName", notification.playerName());
        Validate.requireString("ip", notification.ip());
        Validate.requireString("proxyId", notification.location().proxyId());
        Validate.requireString("serverId", notification.location().serverId());

        service.login(playerId, notification);
    }

    /* The user has disconnected */
    @Delete("/logout")
    public void logout(UUID playerId) {
        service.logout(playerId);
    }
}