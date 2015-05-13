package com.ambergleam.android.paperplane.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

public class DialogUtils {

    public static void showAlertDialog(Activity activity, String message) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    public static Dialog createAlertDialog(Activity activity, String message) {
        return new AlertDialog.Builder(activity)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .create();
    }

}