package com.elliegabel.s.player.auth;

import io.avaje.http.api.Valid;

import java.util.UUID;

@Valid
public record PreLoginRequest(
        UUID playerId,
        String playerIp
) {
}
