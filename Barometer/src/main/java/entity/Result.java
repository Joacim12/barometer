package entity;

public record Result(
        float requests,
        float requestsPerSecondLastSecond,
        float requestsPerSecondLastMinute,
        float requestsPerSecondLastHour) {
}
