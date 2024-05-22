package com.elliegabel.s.http.config.factory;

import com.elliegabel.s.environment.InstanceEnvironment;
import io.avaje.config.Config;
import io.avaje.http.api.AvajeJavalinPlugin;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.avaje.inject.InjectModule;
import io.javalin.Javalin;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import io.javalin.plugin.Plugin;

import java.util.List;
import java.util.Map;

@Factory
@InjectModule(requires = AvajeJavalinPlugin.class)
public class JavalinFactory {

    @Bean
    Javalin javalin(List<Plugin<Void>> plugins) {
        Javalin javalin = Javalin.create(
                cfg -> {
                    // Register plugins (includes routes)
                    plugins.forEach(cfg::registerPlugin);

                    // enable dev logging for dev envs
                    if (!InstanceEnvironment.current().isManaged()) {
                        cfg.bundledPlugins.enableRouteOverview("routes");
                        cfg.bundledPlugins.enableDevLogging();
                    }

                    cfg.router.contextPath = Config.get("CONTEXT_PATH", "/");
                    // todo consider auth?
//                    cfg.router.mount(routing -> {
//                        routing.addBefore(...)
//                    });
                }
        );

        // todo sort this shit out
        javalin.exception(HttpResponseException.class,
                (error, ctx) -> ctx.status(error.getStatus()).json(new ErrorResponse(error.getMessage(), error.getDetails())));

        javalin.exception(RuntimeException.class, (exception, ctx) -> {
            exception.printStackTrace();
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .json(new ErrorResponse(exception.getMessage(), Map.of()));
        });

        return javalin;
    }

    public record ErrorResponse(String error, Map<String, String> details) {
    }
}
