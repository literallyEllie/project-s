package com.elliegabel.s.player.domain.service;

import com.elliegabel.s.log.Log;
import com.elliegabel.s.player.domain.repository.ProfileRepository;
import com.elliegabel.s.player.dto.lookup.PlayerId;
import com.elliegabel.s.player.error.ProfileNotFoundException;
import com.elliegabel.s.player.profile.PlayerProfile;
import com.elliegabel.s.player.profile.PlayerRank;
import io.javalin.http.NotFoundResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;

/**
 * Handles core data about a player.
 */
@Singleton
public class ProfileService {
    private static final Logger LOGGER = Log.newLogger("ProfileService");

    private final ProfileRepository repository;

    @Inject
    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    /**
     * Get the full profile of a player.
     *
     * @param playerId Player id to get the profile of.
     * @return Their profile, or null.
     */
    public PlayerProfile getProfile(@NotNull UUID playerId) {
        LOGGER.info("getProfile {}", playerId);
        return repository.getProfile(playerId).orElseThrow(NotFoundResponse::new);
    }

    public PlayerProfile getProfileByUsername(@NotNull String username) {
        LOGGER.info("getProfileByUsername {}", username);
        return repository.getProfileByName(username).orElseThrow(NotFoundResponse::new);
    }

    /**
     * Get a player's username by their id.
     *
     * @param playerId PLayer id.
     * @return Their username.
     */
    public String getUsernameFromId(@NotNull UUID playerId) {
        LOGGER.info("getUsernameFromId({})", playerId);
        return repository.getUsernameById(playerId).orElseThrow(NotFoundResponse::new);
    }

    public PlayerId getUuidFromName(@NotNull String playerName) {
        LOGGER.info("getUuidFromName({})", playerName);
        return repository.getIdByUsername(playerName).orElseThrow(NotFoundResponse::new);
    }

    /**
     * Handle a login for a player.
     * </br>
     * Creates/updates their profile.
     *
     * @param playerId Player logging in.
     * @param username Their username.
     * @param ip       Their connecting ip.
     */
    public void handleLogin(@NotNull UUID playerId, @NotNull String username, @NotNull String ip) {
        LOGGER.info("handleLogin {} {} {}", playerId, username, ip);

        repository.upsertProfile(playerId, username, ip);
    }

    /**
     * Handle logout from a player.
     * </br>
     * Updates their last seen.
     *
     * @param playerId Player logging out.
     */
    public void handleLogout(@NotNull UUID playerId) {
        LOGGER.info("handleLogout {}", playerId);

        if (!repository.updateLastSeen(playerId)) {
            throw new ProfileNotFoundException(playerId);
        }
    }

    /**
     * Update a player rank.
     *
     * @param playerId Player id to update.
     * @param rank     Their new rank, or null.
     */
    public void updateRank(@NotNull UUID playerId, @NotNull PlayerRank rank) {
        LOGGER.info("updateRank {} -> {}", playerId, rank);

        if (!repository.updateRank(playerId, rank)) {
            throw new ProfileNotFoundException(playerId);
        }
    }
}

