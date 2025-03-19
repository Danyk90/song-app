package com.project.mp3resourceservice.util;

import java.time.Duration;

public class DurationFormatter {



        public static String formatDuration(String duration) {
            double totalSeconds = Double.parseDouble(duration);
            int hours = (int) (totalSeconds / 3600);
            int minutes = (int) ((totalSeconds % 3600) / 60);

            return String.format("%02d:%02d", hours, minutes);
        }
}
