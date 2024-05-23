package com.elliegabel.s.player.util;

import com.elliegabel.s.player.skin.PlayerSkinData;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * Utility for fetching skins from Mojang.
 */
public final class MojangSkinFetcher {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String SKIN_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    @NotNull
    public static PlayerSkinData getSkinData(@NotNull UUID playerId) throws IOException, InterruptedException {
        // https://sessionserver.mojang.com/session/minecraft/profile/65778a9ae3a1412985e7dc57a377515c?unsigned=false
        URI uri = generateUri(playerId);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        HttpResponse<String> resp = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new IOException("Mojang response: " + resp.statusCode());
        }

        return parseResponse(playerId, resp.body());
    }

    private static URI generateUri(UUID playerId) {
        String urlUuid = stripUuid(playerId);
        String url = SKIN_URL.formatted(urlUuid);
        return URI.create(url);
    }

    @NotNull
    private static String stripUuid(@NotNull UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    private static PlayerSkinData parseResponse(UUID playerId, String body) {
        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        JsonObject properties = jsonBody.getAsJsonArray("properties").get(0).getAsJsonObject();

        String skinTextures = properties.get("value").getAsString();
        String skinSignature = properties.get("signature").getAsString();

        return new PlayerSkinData(playerId, skinTextures, skinSignature);
    }
}