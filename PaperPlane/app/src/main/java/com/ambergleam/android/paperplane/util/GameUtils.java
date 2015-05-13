package com.ambergleam.android.paperplane.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentSender;

import com.ambergleam.android.paperplane.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.GamesActivityResultCodes;

import timber.log.Timber;

public class GameUtils {

    /**
     * Resolve a connection failure from
     * {@link com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)}
     *
     * @param activity             the Activity trying to resolve the connection failure.
     * @param client               the GoogleAPIClient instance of the Activity.
     * @param result               the ConnectionResult received by the Activity.
     * @param requestCode          a request code which the calling Activity can use to identify the result
     *                             of this resolution in onActivityResult.
     * @param fallbackErrorMessage a generic error message to display if the failure cannot be resolved.
     * @return true if the connection failure is resolved, false otherwise.
     */
    public static boolean resolveConnectionFailure(
            Activity activity,
            GoogleApiClient client,
            ConnectionResult result,
            int requestCode,
            String fallbackErrorMessage) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(activity, requestCode);
                return true;
            } catch (IntentSender.SendIntentException e) {
                client.connect();
                return false;
            }
        } else {
            int errorCode = result.getErrorCode();
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activity, requestCode);
            if (dialog != null) {
                dialog.show();
            } else {
                DialogUtils.showAlertDialog(activity, fallbackErrorMessage);
            }
            return false;
        }
    }

    /**
     * Show a {@link android.app.Dialog} with the correct message for a connection error.
     *
     * @param activity         the Activity in which the Dialog should be displayed.
     * @param requestCode      the request code from onActivityResult.
     * @param responseCode     the response code from onActivityResult.
     * @param errorDescription the resource id of a String for a generic error message.
     */
    public static void showErrorDialog(
            Activity activity,
            int requestCode,
            int responseCode,
            int errorDescription) {
        if (activity == null) {
            Timber.e("Activity is null.");
            return;
        }
        Dialog dialog;
        switch (responseCode) {
            case GamesActivityResultCodes.RESULT_SIGN_IN_FAILED:
                dialog = DialogUtils.createAlertDialog(activity, activity.getString(R.string.error_auth));
                break;
            case GamesActivityResultCodes.RESULT_APP_MISCONFIGURED:
                dialog = DialogUtils.createAlertDialog(activity, activity.getString(R.string.error_misconfigured));
                break;
            case GamesActivityResultCodes.RESULT_LICENSE_FAILED:
                dialog = DialogUtils.createAlertDialog(activity, activity.getString(R.string.error_license));
                break;
            default:
                final int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
                dialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activity, requestCode, null);
                if (dialog == null) {
                    dialog = DialogUtils.createAlertDialog(activity, activity.getString(errorDescription));
                }
        }
        dialog.show();
    }

}