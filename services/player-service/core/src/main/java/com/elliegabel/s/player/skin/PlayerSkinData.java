package com.elliegabel.s.player.skin;

import java.util.UUID;

/**
 * Represents the unique skin data of a player.
 * </br>
 * This can be updated when the player joins,
 * or it may be fetched externally.
 *
 * @param playerId  Owning player id
 * @param texture   Skin texture
 * @param signature Skin signature.
 */
public record PlayerSkinData(UUID playerId, String texture, String signature) {
}
