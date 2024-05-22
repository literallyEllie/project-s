package com.elliegabel.s.player.profile;

import java.util.UUID;

public record PlayerProfile(
        UUID playerId, String username, String lastIp,
        long firstSeen, long lastSeen,
        PlayerRank rank
) {
}
