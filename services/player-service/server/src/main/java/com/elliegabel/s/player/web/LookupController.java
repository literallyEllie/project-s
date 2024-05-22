package com.elliegabel.s.player.web;

import com.elliegabel.s.http.ApiController;
import com.elliegabel.s.player.domain.service.ProfileService;
import com.elliegabel.s.player.dto.lookup.PlayerId;
import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import jakarta.inject.Inject;

import java.util.UUID;

@Controller("/lookup/")
public class LookupController implements ApiController {

    private final ProfileService service;

    @Inject
    public LookupController(ProfileService service) {
        this.service = service;
    }

    @Get("/id/{playerName}")
    public PlayerId getPlayerId(String playerName) {
        return service.getUuidFromName(playerName);
    }

    @Get("/username/{playerId}")
    public String getPlayerUsername(UUID playerId) {
        return service.getUsernameFromId(playerId);
    }
}
