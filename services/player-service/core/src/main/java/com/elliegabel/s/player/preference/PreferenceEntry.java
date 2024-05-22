package com.elliegabel.s.player.preference;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a tagged preference pair for a player.
 *
 * @param playerId Owning player id.
 * @param preference Preference key.
 * @param option The option set.
 */
public record PreferenceEntry(@NotNull UUID playerId, @NotNull Preference<?> preference, @NotNull PreferenceOption option) {
}