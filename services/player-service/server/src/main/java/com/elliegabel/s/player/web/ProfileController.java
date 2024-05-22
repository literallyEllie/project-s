package com.elliegabel.s.player.web;

import com.elliegabel.s.http.ApiController;
import com.elliegabel.s.http.util.Validate;
import com.elliegabel.s.player.domain.service.ProfileService;
import com.elliegabel.s.player.dto.profile.RankUpdateRequest;
import com.elliegabel.s.player.profile.PlayerProfile;
import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import io.avaje.http.api.Patch;
import jakarta.inject.Inject;

import java.util.UUID;

@Controller("/profile/")
public class ProfileController implements ApiController {

    private final ProfileService service;

    @Inject
    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @Get("/{playerId}")
    public PlayerProfile getProfile(UUID playerId) {
        return service.getProfile(playerId);
    }

    @Patch("/{playerId}/rank")
    public void updateRank(UUID playerId, RankUpdateRequest request) {
        Validate.require(request.rank() != null, "rank is required");

        service.updateRank(playerId, request.rank());
    }

    @Get("/name/{playerName}")
    public PlayerProfile getProfileByName(String playerName) {
        return service.getProfileByUsername(playerName);
    }
}
