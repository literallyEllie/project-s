package com.elliegabel.s.http.web;

import com.elliegabel.s.executor.Executor;
import com.elliegabel.s.http.domain.ComponentProbe;
import com.elliegabel.s.http.probe.ComponentStatus;
import com.elliegabel.s.http.probe.HealthProbeResponse;
import com.elliegabel.s.log.Log;
import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Endpoint to probe the health of a service.
 */
@Controller("/health")
public class HealthController {
    private static final Logger LOGGER = Log.newLogger(HealthController.class);

    private final Map<String, ComponentProbe> probes;

    @Inject
    public HealthController(List<ComponentProbe> probes) {
        this.probes = probes.stream()
                .collect(Collectors.toMap(
                        componentProbe -> componentProbe.id().toLowerCase(),
                        Function.identity())
                );
    }

    /**
     * Health probe provider for a service.
     *
     * @return Health response.
     */
    @Get
    @NotNull
    public CompletableFuture<HealthProbeResponse> probe() {
        Map<String, ComponentStatus> statuses = new HashMap<>();

        List<CompletableFuture<Void>> futures = probes.entrySet().stream()
                .map(entry -> CompletableFuture.runAsync(() -> {
                    String id = entry.getKey();
                    ComponentProbe component = entry.getValue();

                    try {
                        ComponentStatus status = component.probe().get();
                        statuses.put(id, status);
                    } catch (Exception e) {
                        LOGGER.error("Failed to probe {}", id, e);
                        statuses.put(id, ComponentStatus.down(e.getMessage())
                                .critical(component.critical()).build());
                    }
                }, Executor.getExecutor())).toList();

        return CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).thenApply(unused -> new HealthProbeResponse(statuses));
    }

    /**
     * Health probe provider for a service.
     *
     * @param componentId Component id to get the status of.
     * @return Health response.
     */
    @Get("/{componentId}")
    @NotNull
    public CompletableFuture<ComponentStatus> probe(@NotNull String componentId) {
        ComponentProbe component = probes.get(componentId);
        if (component == null) {
            return CompletableFuture.failedFuture(new NullPointerException("no such component"));
        }

        return component.probe()
                .exceptionally(throwable -> ComponentStatus.down(throwable.getMessage())
                        .critical(component.critical()).build());
    }
}