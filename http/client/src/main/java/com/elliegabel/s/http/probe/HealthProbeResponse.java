package com.elliegabel.s.http.probe;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Overall health response of a service.
 */
public class HealthProbeResponse {

    private ComponentStatus.State state;
    private @Nullable Map<String, ComponentStatus> components;

    public HealthProbeResponse() {
    }

    public HealthProbeResponse(ComponentStatus.State masterState, @Nullable Map<String, ComponentStatus> components) {
        this.state = masterState;
        this.components = components;
    }

    public HealthProbeResponse(ComponentStatus.State serviceStatus) {
        this(serviceStatus, null);
    }

    public HealthProbeResponse(@Nullable Map<String, ComponentStatus> components) {
        this(null, components);

        // infer state from subcomponents
        if (components != null) {
            state = components.values().stream()
                    .filter(ComponentStatus::isCritical)
                    .anyMatch(status -> status.getState() == ComponentStatus.State.DOWN)
                    ? ComponentStatus.State.DOWN : ComponentStatus.State.UP;
        }
    }

    /**
     * @return Overall state of the service.
     */
    public ComponentStatus.State getMasterState() {
        return state;
    }

    /**
     * @return Components of the service.
     */
    @Nullable
    public Map<String, ComponentStatus> getComponents() {
        return components;
    }
}