package com.elliegabel.s.player.web;

import com.elliegabel.s.http.ApiController;
import com.elliegabel.s.player.auth.LoginNotification;
import com.elliegabel.s.player.domain.service.PlayerAuthService;
import com.elliegabel.s.player.auth.PreLoginRequest;
import com.elliegabel.s.player.auth.PreLoginResponse;
import io.avaje.http.api.*;
import io.javalin.http.Context;
import jakarta.inject.Inject;

import java.util.UUID;

@Controller("/{playerId}/auth")
public class AuthController implements ApiController {

    private final PlayerAuthService service;

    @Inject
    public AuthController(PlayerAuthService service) {
        this.service = service;
    }

    /* The user wants to log on. */
    @Get("/prelogin")
    public PreLoginResponse preLogin(Context ctx, UUID playerId, PreLoginRequest request) {
        return service.preLogin(playerId, request.playerIp());
    }

    /* The user has logged onto the network. */
    @Post("/login")
    public void login(UUID playerId, @BodyString LoginNotification notification) {
        service.login(
                playerId,
                notification.playerName(),
                notification.ip(),
                notification.proxyId(),
                notification.serverId()
        );
    }

    /* The user has disconnected */
    @Post("/logout")
    public void logout(UUID playerId) {
        service.logout(playerId);
    }
}