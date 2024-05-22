package com.elliegabel.s.http.config;

import io.avaje.config.Config;
import io.avaje.http.api.AvajeJavalinPlugin;
import io.avaje.inject.BeanScope;
import io.avaje.inject.InjectModule;
import io.javalin.Javalin;
import org.jetbrains.annotations.NotNull;

public class ServerBootstrap {

    public static void run() {
        run(BeanScope.builder().build());
    }

    public static void run(@NotNull BeanScope beanScope) {
        var host = Config.get("HTTP_HOST", "127.0.0.1");
        var port = Config.getInt("HTTP_PORT", 8080);

        var javalin = beanScope.get(Javalin.class);
        javalin.start(host, port);
    }
}
