package com.elliegabel.s.player.preference;

public interface Preference<O extends PreferenceOption> {
    String getKey();

    Class<? extends PreferenceOption> getOptions();
}