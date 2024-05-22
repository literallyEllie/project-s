package com.elliegabel.s.player.dto.auth;

import io.avaje.http.api.Valid;

@Valid
public record PreLoginRequest(
        String playerIp
) {
}
