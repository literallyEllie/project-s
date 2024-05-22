package com.elliegabel.s.player.domain.service;

import com.elliegabel.s.log.Log;
import com.elliegabel.s.player.domain.repository.LocationRepository;
import com.elliegabel.s.player.location.PlayerLocation;
import io.javalin.http.NotFoundResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Keeps track of where players are on the network.
 */
@Singleton
public class LocationService {
    private static final Logger LOGGER = Log.newLogger("LocationService");
    private final LocationRepository repository;

    @Inject
    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    /* Individual */

    public PlayerLocation getLocation(@NotNull UUID playerId) {
        return repository.getById(playerId).orElseThrow(NotFoundResponse::new);
    }

    public void setLocation(@NotNull UUID playerId, PlayerLocation location) {
        LOGGER.info("{} -> {}/{}", playerId, location.proxyId(), location.serverId());
        repository.setFullLocation(playerId, location.proxyId(), location.serverId());
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

    /* Group */

    public List<PlayerLocation> getPlayers() {
        return repository.getAll();
    }

    public List<UUID> getPlayersOnProxy(@NotNull String proxyId) {
        return repository.getPlayersOnProxy(proxyId);
    }

    public List<UUID> getPlayersOnServer(@NotNull String serverId) {
        return repository.getPlayersOnServer(serverId);
    }
}
