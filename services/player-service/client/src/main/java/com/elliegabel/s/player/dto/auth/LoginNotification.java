package com.elliegabel.s.player.dto.auth;

import com.elliegabel.s.player.location.PlayerLocation;
import io.avaje.http.api.Valid;

@Valid
public record LoginNotification(
        String playerName, String ip, PlayerLocation location
) {
}
