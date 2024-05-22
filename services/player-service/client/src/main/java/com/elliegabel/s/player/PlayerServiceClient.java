package com.elliegabel.s.player;

import com.elliegabel.s.player.dto.auth.LoginNotification;
import com.elliegabel.s.player.dto.auth.PreLoginResponse;
import com.elliegabel.s.player.dto.lookup.PlayerId;
import com.elliegabel.s.player.dto.profile.RankUpdateRequest;
import com.elliegabel.s.player.location.PlayerLocation;
import com.elliegabel.s.player.profile.PlayerProfile;
import com.elliegabel.s.player.skin.PlayerSkinData;
import io.avaje.http.api.*;

import java.util.List;
import java.util.UUID;

/**
 * Client for interacting with the player-service.
 * </br>
 * All fields should be presumed NotNull unless specified.
 */
@Client
public interface PlayerServiceClient {

    /* Auth */

    @Get("/auth/{playerId}/prelogin")
    PreLoginResponse preLogin(UUID playerId, String ip);

    @Post("/auth/{playerId}/login")
    void login(UUID playerId, LoginNotification notification);

    @Delete("/auth/{playerId}/logout")
    void logout(UUID playerId);

    /* Profile */

    @Get("/profile/{playerId}")
    PlayerProfile getProfile(UUID playerId);

    @Patch("/profile/{playerId}/rank")
    void updateRank(UUID playerId, RankUpdateRequest rank);

    @Get("/profile/name/{playerName}")
    PlayerProfile getProfileByName(String playerName);

    /* Lookup */

    @Get("/lookup/id/{playerName}")
    PlayerId getPlayerId(String playerName);

    @Get("/lookup/username/{playerId}")
    String getPlayerUsername(UUID playerId);

    /* Individual Location */

    @Get("/location/{playerId}")
    PlayerLocation getPlayerLocation(UUID playerId);

    @Put("/location/{playerId}")
    void setPlayerLocation(UUID playerId, PlayerLocation location);

    @Delete("/location/{playerId}")
    void unsetPlayerLocation(UUID playerId);

    @Get("/location/{playerId}/proxy")
    String getPlayerProxy(UUID playerId);

    @Get("/location/{playerId}/server")
    String getPlayerServer(UUID playerId);

    @Patch("/location/{playerId}/server")
    void setPlayerServer(UUID playerId, String serverId);

    /* Group location */

    @Get("/players/proxy/{proxyId}")
    List<UUID> getPlayersOnProxy(String proxyId);

    @Get("/players/server/{serverId}")
    List<UUID> getPlayersOnServer(String serverId);

    /* Skins */

    @Get("/skin/{playerId}")
    PlayerSkinData getSkin(UUID playerId, @QueryParam boolean fetchExternal);

    @Put("/skin/{playerId}")
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
