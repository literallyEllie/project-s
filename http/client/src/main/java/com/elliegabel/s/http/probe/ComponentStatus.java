package com.elliegabel.s.http.probe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of a component, and its subcomponents.
 */
public class ComponentStatus {

    private State state;
    private boolean critical;
    private @Nullable Map<String, String> details;
    private @Nullable Map<String, ComponentStatus> subComponents;

    public static Builder builder() {
        return new Builder();
    }

    public static ComponentStatus.Builder up() {
        return builder().status(State.UP);
    }

    public static ComponentStatus.Builder down(String error) {
        return builder().status(State.DOWN).error(error);
    }

    public ComponentStatus(
            State state, boolean critical,
            @Nullable Map<String, String> details, @Nullable Map<String, ComponentStatus> subComponents
    ) {
        this.state = state;
        this.critical = critical;
        this.details = details;
        this.subComponents = subComponents;
    }

    public ComponentStatus() {
    }

    @NotNull
    public State getState() {
        return state;
    }

    public boolean isCritical() {
        return critical;
    }

    @Nullable
    public Map<String, String> getDetails() {
        return details;
    }

    @Nullable
    public Map<String, ComponentStatus> getSubComponents() {
        return subComponents;
    }

    public static enum State {
        UP, DOWN
    }

    public static class Builder {
        private State state;
        private boolean critical;
        private @Nullable Map<String, String> details;
        private @Nullable Map<String, ComponentStatus> subComponents;

        public Builder status(State state) {
            this.state = state;
            return this;
        }

        public Builder detail(String key, String value) {
            if (details == null) {
                details = new HashMap<>();
            }

            details.put(key, value);
            return this;
        }

        public Builder error(String error) {
            return detail("error", error);
        }

        public Builder subComponent(String componentId, ComponentStatus status) {
            if (subComponents == null) {
                subComponents = new HashMap<>();
            }

            subComponents.put(componentId, status);
            return this;
        }

        public Builder critical(boolean critical) {
            this.critical = critical;
            return this;
        }

        public Builder critical() {
            return critical(true);
        }

        public ComponentStatus build() {
            if (state == null && (subComponents == null || subComponents.isEmpty())) {
                throw new IllegalStateException("cannot infer state");
            }

            if (state == null) {
                // infer state by checking status of critical subcomponents
                state = subComponents.values().stream()
                        .filter(ComponentStatus::isCritical)
                        .anyMatch(status -> status.state == State.DOWN) ? State.UP : State.DOWN;
            }

            return new ComponentStatus(state, critical, details, subComponents);
        }
    }
}
