package dk.vetterlain;

import dk.vetterlain.entity.Result;
import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.Context;

import java.time.Instant;

import java.util.List;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static dk.vetterlain.utils.BaroUtils.requestsPerX;

public class Barometer implements Plugin {

    private static final Queue<Instant> globalQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void apply(Javalin javalin) {
        javalin.after(this::handler);
    }

    void handler(Context context) {
//        System.out.println(context.path());
        globalQueue.add(Instant.now());
    }

    public Result getData() {
        List<Instant> copyList = globalQueue.stream().toList();
        float perSecond = requestsPerX(1, copyList);
        float perMinute = requestsPerX(60, copyList) / 60;
        float perHour = (requestsPerX(60 * 60, copyList) / 60) / 60;
        return new Result(copyList.size(), perSecond, perMinute, perHour);
    }


}
