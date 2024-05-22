package com.elliegabel.s.player.dto.profile;

import com.elliegabel.s.player.profile.PlayerRank;
import io.avaje.http.api.Valid;

@Valid
public record RankUpdateRequest(PlayerRank rank) {
}
