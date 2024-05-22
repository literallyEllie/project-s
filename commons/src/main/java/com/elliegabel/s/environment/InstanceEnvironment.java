package com.elliegabel.s.environment;

import io.avaje.config.Config;

/**
 * The environment an instance is set up in.
 */
public enum InstanceEnvironment {
    LOCAL,
    DEVELOPMENT,
    STAGING(true),
    PRODUCTION(true);

    private final boolean isManaged;

    InstanceEnvironment(boolean isManaged) {
        this.isManaged = isManaged;
    }

    InstanceEnvironment() {
        this(false);
    }

    /**
     * @return If the instance is in an automated environment.
     */
    public boolean isManaged() {
        return isManaged;
    }

    private static InstanceEnvironment current;

    public static InstanceEnvironment current() {
        if (current == null) {
            current = Config.getEnum(InstanceEnvironment.class, "environment", InstanceEnvironment.LOCAL);
        }

        return current;
    }

    public static boolean compare(InstanceEnvironment expect) {
        return current() == expect;
    }
}