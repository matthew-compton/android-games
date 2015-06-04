package com.ambergleam.android.paperplane.util;

import android.content.Context;
import android.graphics.Typeface;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class FontUtils {

    /*
     * Typeface
     */
    public static Typeface getTypefaceRegular(Context context) {
        return TypefaceUtils.load(context.getAssets(), getTypefaceRegularPath());
    }

    /*
     * Paths
     */
    public static String getTypefaceRegularPath() {
        return "fonts/VT323-Regular.ttf";
    }

}
