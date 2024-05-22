package com.elliegabel.s.http.config.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.avaje.inject.Secondary;

/**
 * Default GSON provider.
 */
@Secondary
@Factory
public class GsonFactory {

    @Bean
    Gson gson() {
        return new GsonBuilder().create();
    }
}
