package com.elliegabel.s.player.web;

import com.elliegabel.s.http.ApiController;
import com.elliegabel.s.player.skin.PlayerSkinData;
import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import io.avaje.http.api.Put;
import io.avaje.http.api.QueryParam;

import java.util.UUID;

@Controller("/skin/{playerId}")
public class SkinController implements ApiController {

    @Get
    public PlayerSkinData getSkin(UUID playerId, @QueryParam boolean fetchExternal) {
        // TODO
        return null;
    }

    @Put
    public void updateSkin(UUID playerId, PlayerSkinData data) {
        // TODO
    }
}
