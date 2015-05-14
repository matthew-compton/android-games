package com.ambergleam.android.paperplane.util;

public class TimeUtils {

    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;

    public static final int MILLISECONDS_PER_MINUTE = SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
    public static final int MILLISECONDS_PER_HOUR = MINUTES_PER_HOUR * MILLISECONDS_PER_MINUTE;

    public static String formatTime(long milliseconds) {
        String formattedTime;
        String format;
        long seconds = (milliseconds / MILLISECONDS_PER_SECOND) % 60;
        long minutes = (milliseconds / MILLISECONDS_PER_MINUTE) % 60;
        long hours = (milliseconds / MILLISECONDS_PER_HOUR);
        if (hours == 0 && minutes == 0) {
            format = "0m %ds";
            formattedTime = String.format(format, seconds);
        } else if (hours == 0) {
            format = "%dm %ds";
            formattedTime = String.format(format, minutes, seconds);
        } else {
            format = "%dh %dm %ds";
            formattedTime = String.format(format, hours, minutes, seconds);
        }
        return formattedTime;
    }

}