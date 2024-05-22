package com.elliegabel.s.player.web;

import com.elliegabel.s.http.ApiController;
import com.elliegabel.s.http.util.Validate;
import com.elliegabel.s.player.domain.service.LocationService;
import com.elliegabel.s.player.location.PlayerLocation;
import io.avaje.http.api.*;
import jakarta.inject.Inject;

import java.util.UUID;

/**
 * Player indexed location operations.
 * As opposed to server location operations see ServerLocationController.
 */
@Controller("/location/{playerId}")
public class PlayerLocationController implements ApiController  {

    private final LocationService locationService;

    @Inject
    public PlayerLocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /* Current location of the player */
    @Get
    public PlayerLocation getLocation(UUID playerId) {
        return locationService.getLocation(playerId);
    }

    @Put
    public void setLocation(UUID playerId, PlayerLocation location) {
        Validate.requireString("proxyId", location.proxyId());
        Validate.requireString("serverId", location.serverId());

        locationService.setLocation(playerId, location);
    }

    @Delete
    public void unset(UUID playerId) {
        locationService.unsetLocation(playerId);
    }

    @Get("/proxy")
    public String getProxy(UUID playerId) {
        return locationService.getProxyId(playerId);
    }

    @Get("/server")
    public String getServer(UUID playerId) {
        return locationService.getServerId(playerId);
    }

    @Patch("/server")
    public void setServer(UUID playerId, String serverId) {
        Validate.requireString("serverId", serverId);

        locationService.setServerId(playerId, serverId);
    }
}
