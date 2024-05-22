package com.elliegabel.s.player.error;

import io.javalin.http.NotFoundResponse;

import java.util.UUID;

public class PlayerOfflineException extends NotFoundResponse {

    public PlayerOfflineException(UUID playerId) {
        super(playerId + " is not online");
    }
}
