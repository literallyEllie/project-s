package com.elliegabel.s.http.domain;

import com.elliegabel.s.http.probe.ComponentStatus;

import java.util.concurrent.CompletableFuture;

/**
 * Represents something which can be probed.
 */
public interface ComponentProbe {

    /**
     * @return Id of the component.
     */
    String id();

    /**
     * @return If this component is critical.
     */
    boolean critical();

    /**
     * @return Probe the response and return a response.
     */
    CompletableFuture<ComponentStatus> probe();
}
