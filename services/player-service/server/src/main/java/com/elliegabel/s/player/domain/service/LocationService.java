package com.elliegabel.s.player.domain.service;

import com.elliegabel.s.log.Log;
import com.elliegabel.s.player.domain.repository.LocationRepository;
import com.elliegabel.s.player.location.PlayerLocation;
import io.javalin.http.NotFoundResponse;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

/**
 * Keeps track of where players are on the network.
 */
@Singleton
public class LocationService {
    private static final Logger LOGGER = Log.newLogger("LocationService");
    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    // todo methods to get where players are

    public PlayerLocation getLocation(@NotNull UUID playerId) {
        return repository.getById(playerId).orElseThrow(NotFoundResponse::new);
    }

    public void setLocation(@NotNull UUID playerId, @NotNull String proxyId, @NotNull String serverId) {
        LOGGER.info("{} -> {}/{}", playerId, proxyId, serverId);
        repository.setFullLocation(playerId, proxyId, serverId);
    }

    public void unsetLocation(@NotNull UUID playerId) {
        LOGGER.info("unset {}", playerId);
        repository.deleteLocation(playerId);
    }

    public String getProxyId(@NotNull UUID playerId) {
        return repository.getProxyId(playerId).orElseThrow(NotFoundResponse::new);
    }

    public String getServerId(@NotNull UUID playerId) {
        return repository.getServerId(playerId).orElseThrow(NotFoundResponse::new);
    }

    public void setServerId(@NotNull UUID playerId, @NotNull String serverId) {
        LOGGER.info("{} -> {}", playerId, serverId);
        repository.setLocation(playerId, serverId);
    }
}
