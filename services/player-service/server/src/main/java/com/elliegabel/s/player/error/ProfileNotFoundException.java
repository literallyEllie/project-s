package com.elliegabel.s.player.error;

import io.javalin.http.NotFoundResponse;

import java.util.UUID;

/**
 * Exception thrown when a requested profile does not exist.
 */
public class ProfileNotFoundException extends NotFoundResponse {

    public ProfileNotFoundException(UUID playerId) {
        super(playerId + " does not exist");
    }

    public ProfileNotFoundException(String username) {
        super(username + " does not exist");
    }
}
