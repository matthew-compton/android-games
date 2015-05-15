package com.ambergleam.android.paperplane.util;

import java.text.DecimalFormat;

public class DistanceUtils {

    private static final String DECIMAL_FORMAT_DISTANCE = "#,###,###";

    public static String formatDistance(long distance_ft) {
        DecimalFormat formatter = new DecimalFormat(DECIMAL_FORMAT_DISTANCE);
        String formattedDistance = formatter.format(distance_ft).concat(" ft");
        return formattedDistance;
    }

}