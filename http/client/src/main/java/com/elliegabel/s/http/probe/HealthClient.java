package com.elliegabel.s.http.probe;

import io.avaje.http.api.Client;
import io.avaje.http.api.Get;

@Client
public interface HealthClient {

    @Get("/health")
    HealthProbeResponse getHealth();

    @Get("/health/{componentId}")
    HealthProbeResponse getComponentHealth(String componentId);
}
