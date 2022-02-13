package dk.vetterlain.rest;

import io.javalin.http.Context;

public class Controller {

    public static void visitors(Context context) {
        context.result(1 + "");
    }

}
