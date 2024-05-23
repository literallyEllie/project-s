package com.elliegabel.s.player.web;

import com.elliegabel.s.http.ApiController;
import com.elliegabel.s.http.util.Validate;
import com.elliegabel.s.player.domain.service.SkinService;
import com.elliegabel.s.player.skin.PlayerSkinData;
import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import io.avaje.http.api.Put;
import io.avaje.http.api.QueryParam;
import jakarta.inject.Inject;

import java.util.UUID;

@Controller("/skin/{playerId}")
public class SkinController implements ApiController {
    private final SkinService skinService;

    @Inject
    public SkinController(SkinService skinService) {
        this.skinService = skinService;
    }

    @Get
    public PlayerSkinData getSkin(UUID playerId, @QueryParam(value = "false") boolean fetchExternal) {
        return skinService.getSkin(playerId, fetchExternal);
    }

    @Put
    public void updateSkin(UUID playerId, PlayerSkinData data) {
        Validate.require(playerId.equals(data.playerId()), "playerIds do not match");
        Validate.require(data.texture() != null && data.texture().length() == 432, "invalid texture");
        Validate.require(data.signature() != null && data.signature().length() == 686, "invalid signature");

        skinService.registerSkin(data);
    }
}
