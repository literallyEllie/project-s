package com.elliegabel.s.log;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger utility.
 */
public final class Log {

    @NotNull
    public static Logger newLogger(@NotNull Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    @NotNull
    public static Logger newLogger(@NotNull String name) {
        return LoggerFactory.getLogger(name);
    }

    @NotNull
    public static Logger newLogger(@NotNull Object object) {
        return LoggerFactory.getLogger(object.getClass());
    }
}
