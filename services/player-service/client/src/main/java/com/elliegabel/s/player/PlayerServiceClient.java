package com.elliegabel.s.player;

import com.elliegabel.s.player.auth.PreLoginResponse;
import com.elliegabel.s.player.preference.Preference;
import com.elliegabel.s.player.preference.PreferenceOption;
import com.elliegabel.s.player.profile.PlayerProfile;
import com.elliegabel.s.player.profile.PlayerRank;
import com.elliegabel.s.player.skin.PlayerSkinData;
import io.avaje.http.api.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Client for interacting with the player-service.
 * </br>
 * All fields should be presumed NotNull unless specified.
 */
@Client
public interface PlayerServiceClient {

    /* Auth */

    @Get("/player/{playerId}/auth/prelogin")
    PreLoginResponse preLogin(UUID playerId, @QueryParam String ip);

    @Post("/player/{playerId}/auth/login")
    void login(UUID playerId, @QueryParam String playerName, @QueryParam String ip, @QueryParam String proxyId, @QueryParam String serverId);

    @Post("/player/{playerId}/auth/logout")
    void logout(UUID playerId);

    /* Profile */

    @Get("/player/{playerId}")
    PlayerProfile getProfile(UUID playerId);

    @Get("/player/name/{playerName}/profile")
    PlayerProfile getProfileByName(String playerName);

    @Patch("/player/{playerId}/rank/{rank}")
    void updateRank(UUID playerId, PlayerRank rank);

    /* Lookup */

    @Get("/player/name/{playerName}")
    UUID getPlayerId(String playerName);

    @Get("/player/id/{playerId}")
    String getPlayerUsername(UUID playerId);

    /* Skins */

    @Get("/player/{playerId}/skin")
    PlayerSkinData getSkin(UUID playerId, @QueryParam boolean fetchExternal);

    @Post("/player/{playerId}/skin")
    void updateSkin(UUID playerId, PlayerSkinData data);

    /* Preferences */
//
//    @Get("/player/{playerId}/preferences")
//    Map<Preference<?>, PreferenceOption> getPreferences(
//            UUID playerId,
//            @Nullable @QueryParam("keys") List<Preference<?>> keys
//    );
//
//    @Get("/player/{playerId}/preference/{preference}")
//    <T extends PreferenceOption> PreferenceOption getPreference(UUID playerId, Preference<T> preference);
//
//    @Patch("/player/{playerId}/preference/{preference}/{option}")
//    <T extends PreferenceOption> void setPreference(UUID playerId, Preference<T> preference, PreferenceOption option);
//
//    @Delete("/player/{playerId}/preference/{preference}")
//    <T extends PreferenceOption> void deletePreference(UUID playerId, Preference<T> preference);
}
