package utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class BaroUtils {

    public static float requestsPerX(int seconds, List<Instant> list) {
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
