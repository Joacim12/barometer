
import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.Context;


import java.time.Duration;
import java.time.Instant;

import java.util.List;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


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

    public static void handle(Context context) {
        List<Instant> l = globalQueue.stream().toList();
        float perSecond = requestsPerX(1, l);
        float perMinute = requestsPerX(60, l) / 60;
        float perHour = (requestsPerX(60 * 60, l) / 60) / 60;
        context.json(new Result(l.size(), perSecond, perMinute, perHour));
    }


    static float requestsPerX(int seconds, List<Instant> list) {
        Instant now = Instant.now();
        float count = 0;

        // Iterate the instants from the end
        for (int i = list.size() - 1; i >= 0; i--) {
            // If instant is before the given second value, break out of the loop
            if (list.get(i).isBefore(now.minus(Duration.ofSeconds(seconds)))) break;
            // Count!!
            count++;
        }

        // This must be slower, when the list gets big
//        for (Instant then : globalQueue) {
//            Duration d = Duration.between(then, now);
//            if (d.getSeconds() < seconds) {
//                sum++;
//            }
//        }
        return count;
    }

}

record Result(
        float requests,
        float requestsPerSecondLastSecond,
        float requestsPerSecondLastMinute,
        float requestsPerSecondLastHour) {
}
