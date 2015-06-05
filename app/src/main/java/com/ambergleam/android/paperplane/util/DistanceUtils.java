package com.ambergleam.android.paperplane.util;

import java.text.DecimalFormat;

public class DistanceUtils {

    private static final String DECIMAL_FORMAT_DISTANCE = "#,###,##0.000";

    public static String formatDistance(int distance_m) {
        double distance_km = distance_m / 1000.0;
        DecimalFormat formatter = new DecimalFormat(DECIMAL_FORMAT_DISTANCE);
        return formatter.format(distance_km).concat(" km");
    }

}