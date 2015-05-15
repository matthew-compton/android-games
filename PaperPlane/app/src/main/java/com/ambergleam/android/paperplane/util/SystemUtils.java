package com.ambergleam.android.paperplane.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ambergleam.android.paperplane.util.systemuihelper.SystemUiHelper;

public class SystemUtils {

    public static final boolean IS_LOLLIPOP_AND_UP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    public static void hideSystemUI(Activity activity) {
        SystemUiHelper helper = new SystemUiHelper(activity, SystemUiHelper.LEVEL_IMMERSIVE, SystemUiHelper.FLAG_IMMERSIVE_STICKY);
        helper.hide();
    }

    public static void showSystemUI(Activity activity) {
        SystemUiHelper helper = new SystemUiHelper(activity, SystemUiHelper.LEVEL_IMMERSIVE, SystemUiHelper.FLAG_IMMERSIVE_STICKY);
        helper.show();
    }

    public static void hideSoftwareKeyboard(Activity activity) {
        hideSoftwareKeyboard(activity, activity.getCurrentFocus());
    }

    public static void hideSoftwareKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}