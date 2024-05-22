package com.elliegabel.s.player.auth;

public record LoginNotification(
        String playerName, String ip,
        String proxyId, String serverId
) {
}
