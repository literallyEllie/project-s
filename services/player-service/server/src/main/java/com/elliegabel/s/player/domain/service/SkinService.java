package com.elliegabel.s.player.domain.service;

import com.elliegabel.s.log.Log;
import com.elliegabel.s.player.domain.repository.SkinRepository;
import com.elliegabel.s.player.skin.PlayerSkinData;
import com.elliegabel.s.player.util.MojangSkinFetcher;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class SkinService {
    private static final Logger LOGGER = Log.newLogger(SkinService.class);

    private final SkinRepository repository;

    @Inject
    public SkinService(SkinRepository repository) {
        this.repository = repository;
    }
    /**
     * Get the skin of a player.
     *
     * @param playerId Player id.
     * @param external If to check externally if we don't have their skin.
     * @return The skin data.
     */
    @NotNull
    public PlayerSkinData getSkin(@NotNull UUID playerId, boolean external) {
        Optional<PlayerSkinData> skinData = repository.getSkinData(playerId);

        // If it's not in our cache, check with mojang.
        if (skinData.isEmpty() && external) {
            try {
                // get and put in our database.
                PlayerSkinData mojangData = MojangSkinFetcher.getSkinData(playerId);
                registerSkin(mojangData);

                return mojangData;
            } catch (IOException | InterruptedException e) {
                LOGGER.error("failed to fetch skin for player {}", playerId, e);
                throw new InternalServerErrorResponse();
            }
        }

        return skinData.orElseThrow(NotFoundResponse::new);
    }

    public void registerSkin(PlayerSkinData data) {
        if (!repository.insertSkinData(data)) {
            throw new InternalServerErrorResponse("failed to insert skin");
        }
    }
}
