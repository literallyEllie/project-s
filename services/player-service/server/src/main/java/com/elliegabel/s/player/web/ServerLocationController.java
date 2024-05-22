package com.elliegabel.s.player.web;

import com.elliegabel.s.http.ApiController;
import com.elliegabel.s.http.util.Validate;
import com.elliegabel.s.player.domain.service.LocationService;
import com.elliegabel.s.player.location.PlayerLocation;
import io.avaje.http.api.*;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Server indexed location operations.
 * As opposed to player location operations see PlayerLocationController.
 */
@Controller("/players")
public class ServerLocationController implements ApiController  {

    private final LocationService locationService;

    @Inject
    public ServerLocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /* All players on a proxy */
    @Get("/proxy/{proxyId}")
    public List<UUID> getOnProxy(String proxyId) {
        return locationService.getPlayersOnProxy(proxyId);
    }

    /* All players on a proxy */
    @Get("/server/{serverId}")
    public List<UUID> getOnServer(String serverId) {
        return locationService.getPlayersOnServer(serverId);

    }
//    @Delete("/proxy/{proxyId}")
//    public void deleteProxy(String proxyId) {
//        // TODO
//    }
//
//    @Delete("/server/{serverId}")
//    public void deleteServer(String serverId) {
//        // TODO
//    }
}
