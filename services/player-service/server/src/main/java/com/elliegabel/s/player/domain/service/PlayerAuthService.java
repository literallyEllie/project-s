package com.elliegabel.s.player.domain.service;

import com.elliegabel.s.log.Log;
import com.elliegabel.s.player.dto.auth.LoginNotification;
import com.elliegabel.s.player.dto.auth.PreLoginResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

@Singleton
public class PlayerAuthService {
    private static final Logger LOGGER = Log.newLogger("AuthService");

    private final ProfileService profileService;
    private final LocationService locationService;

    @Inject
    public PlayerAuthService(ProfileService profileService, LocationService locationService) {
        this.profileService = profileService;
        this.locationService = locationService;
    }

    /**
     * Handles pre-login request for a player
     * before they're redirected to a server.
     * </br>
     * TODO This should check against bans and stuff.
     *
     * @param playerId Player id logging on.
     * @param ip Their ip.
     * @return If they can log in or not.
     */
    @NotNull
    public PreLoginResponse preLogin(@NotNull UUID playerId, @NotNull String ip) {
        LOGGER.info("preLogin {} {} -> OK", playerId, ip);
        return PreLoginResponse.OK;
    }

    /**
     * Handle a login for a player.
     * </br>
     * Creates/updates their profile,
     * and adds them to the tracking.
     *
     * @param playerId Player logging in.
     * @param notification Notification
     */
    public void login(@NotNull UUID playerId, LoginNotification notification) {
        profileService.handleLogin(playerId, notification.playerName(), notification.ip());
        locationService.setLocation(playerId, notification.location());
    }

    /**
     * Handle logout from a player.
     * </br>
     * Updates their last seen
     * and removes them from tracking.
     *
     * @param playerId Player logging out.
     */
    public void logout(UUID playerId) {
        LOGGER.info("logout {}", playerId);

        profileService.handleLogout(playerId);
        locationService.unsetLocation(playerId);
    }
}
