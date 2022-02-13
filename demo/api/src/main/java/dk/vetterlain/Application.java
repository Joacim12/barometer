package dk.vetterlain;

import dk.vetterlain.rest.Controller;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import org.slf4j.impl.SimpleLogger;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Application {
    private final static int PORT = 7000;
    private final static String LOG_LEVEL = "warn"; // Must be one of ("trace", "debug", "info", "warn", or "error").

    public static void main(String[] args) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, LOG_LEVEL);
        System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "false");
        System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
        System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd HH:mm:ss:SSS Z");

        Barometer barometer = new Barometer();

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.registerPlugin(barometer);
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.defaultContentType = ContentType.JSON;
        }).start(PORT);

        app.routes(() -> {
            path("api", () -> {
                path("v1", () -> {

                    path("visitors", () -> {
                        get(Controller::visitors);
                    });

                });
                get("/stats", context -> context.json(barometer.getData()));
            });
        });
    }
}
