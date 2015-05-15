package com.ambergleam.android.paperplane.util;

public class TimeUtils {

    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;

    public static final int MILLISECONDS_PER_MINUTE = SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
    public static final int MILLISECONDS_PER_HOUR = MINUTES_PER_HOUR * MILLISECONDS_PER_MINUTE;

    public static String formatTime(int time_ms) {
        String formattedTime;
        String format;
        long milliseconds = (time_ms % MILLISECONDS_PER_SECOND);
        long seconds = (time_ms / MILLISECONDS_PER_SECOND) % 60;
        long minutes = (time_ms / MILLISECONDS_PER_MINUTE) % 60;
        long hours = (time_ms / MILLISECONDS_PER_HOUR);
        if (hours == 0 && minutes == 0) {
            format = "%d.%03d s";
            formattedTime = String.format(format, seconds, milliseconds);
        } else if (hours == 0) {
            format = "%d:%d.%03d";
            formattedTime = String.format(format, minutes, seconds, milliseconds);
        } else {
            format = "%d:%d:%d.%03d";
            formattedTime = String.format(format, hours, minutes, seconds, milliseconds);
        }
        return formattedTime;
    }

}