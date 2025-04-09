package com.project.mp3resourceservice.util;

import java.time.Duration;

public class DurationFormatter {

    public static String formatDuration(String strseconds) {
        double seconds = Double.parseDouble(strseconds);
        Duration duration = Duration.ofMillis((long) (seconds * 1000));
        //long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long secs = duration.toSecondsPart();
        return String.format("%02d:%02d", minutes, secs);
    }
}
