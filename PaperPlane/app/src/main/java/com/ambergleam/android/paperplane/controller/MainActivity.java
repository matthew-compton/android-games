package com.ambergleam.android.paperplane.controller;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;

import com.ambergleam.android.paperplane.BaseApplication;
import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.event.EventHelper;
import com.ambergleam.android.paperplane.event.LoadDataFailureEvent;
import com.ambergleam.android.paperplane.event.LoadDataSuccessEvent;
import com.ambergleam.android.paperplane.event.LoadLeaderboardDistanceFailureEvent;
import com.ambergleam.android.paperplane.event.LoadLeaderboardDistanceSuccessEvent;
import com.ambergleam.android.paperplane.event.LoadLeaderboardTimeFailureEvent;
import com.ambergleam.android.paperplane.event.LoadLeaderboardTimeSuccessEvent;
import com.ambergleam.android.paperplane.manager.DataManager;
import com.ambergleam.android.paperplane.util.DialogUtils;
import com.ambergleam.android.paperplane.util.GameUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.plus.Plus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends FragmentActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MenuFragment.Callbacks,
        GameplayFragment.Callbacks {

    private static final int REQUEST_CODE_SIGNIN = 0;
    private static final int REQUEST_CODE_UNUSED = 1;

    private static final int LOAD_COUNT_TOTAL = 2;

    @Inject DataManager mDataManager;

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure;
    private boolean mSignInClicked;
    private boolean mAutoStartSignInFlow;

    private int mCurrentLoadCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApplication.get(this).inject(this);
        ButterKnife.inject(this);
        EventHelper.registerSubscriber(this);

        setupGoogleApiClient();
        setupOverviewScreen();
        setupState();
        updateFragment(MenuFragment.newInstance(isSignedIn(), getPlayerName()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventHelper.unregisterSubscriber(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_SIGNIN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                GameUtils.showErrorDialog(this, requestCode, resultCode, R.string.error_auth);
            }
        }
    }

    private void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    private void setupState() {
        mResolvingConnectionFailure = false;
        mSignInClicked = false;
        mAutoStartSignInFlow = false;
    }

    private void setupOverviewScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int color = typedValue.data;

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }
    }

    private void updateFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            setFragmentTransactionAnimation(ft);
            ft.replace(R.id.activity_main_container, fragment);
            ft.commit();
        } else {
            Timber.e("Error in creating fragment.");
        }
    }

    private void setFragmentTransactionAnimation(FragmentTransaction ft) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
        if (fragment instanceof MenuFragment) {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (fragment instanceof GameplayFragment) {
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            // Nothing
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Timber.d("Connected to Google APIs");
        updateUI();
        saveData();
        loadData();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Attempting to connect");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Timber.d("Attempting to resolve");
        if (mResolvingConnectionFailure) {
            Timber.d("Already resolving");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            if (!GameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult, REQUEST_CODE_SIGNIN, getString(R.string.error_auth))) {
                mResolvingConnectionFailure = false;
            }
        }
        updateUI();
    }

    @Override
    public void onStartGameRequested() {
        updateFragment(GameplayFragment.newInstance());
    }

    private void showQuitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.fragment_menu_quit_dialog_title))
                .setMessage(getString(R.string.fragment_menu_quit_dialog_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_CODE_UNUSED);
        } else {
            DialogUtils.showAlertDialog(this, getString(R.string.auth_achievements_unavailable));
        }
    }

    @Override
    public void onShowLeaderboardsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), REQUEST_CODE_UNUSED);
        } else {
            DialogUtils.showAlertDialog(this, getString(R.string.auth_leaderboards_unavailable));
        }
    }

    @Override
    public void onSignInRequested() {
        Timber.d("Sign in requested.");
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void onSignOutRequested() {
        Timber.d("Sign out requested.");
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.fragment_menu_sign_out_dialog_title))
                .setMessage(getString(R.string.fragment_menu_sign_out_dialog_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveData();
                        signout();
                        updateUI();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void signout() {
        mDataManager.reset();
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onGameover(int time, int distance) {
        mDataManager.update(time, distance);
        saveData();
        updateFragment(MenuFragment.newInstance(isSignedIn(), getPlayerName()));
    }

    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    private void updateUI() {
        // Update currently displayed Fragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
        if (fragment instanceof MenuFragment) {
            MenuFragment menuFragment = (MenuFragment) fragment;
            menuFragment.setSignedIn(isSignedIn());
            menuFragment.setWelcomeMessage(getPlayerName());
            menuFragment.updateUI();
        } else if (fragment instanceof GameplayFragment) {
            GameplayFragment gameplayFragment = (GameplayFragment) fragment;
            gameplayFragment.pause();
        } else {
            Timber.e("Error with updating current Fragment.");
        }
    }

    private String getPlayerName() {
        if (!isSignedIn()) {
            return null;
        }
        Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);
        return player != null ? player.getDisplayName() : null;
    }

    private void saveData() {
        if (isSignedIn()) {
            mDataManager.save(this, mGoogleApiClient);
        } else {
            Timber.e("Must be signed in to save data.");
        }
    }

    private void loadData() {
        if (isSignedIn()) {
            mDataManager.load(this, mGoogleApiClient);
        } else {
            loadDataFailureEvent();
        }
    }

    public void onEventMainThread(LoadDataSuccessEvent event) {
        updateUI();
        Timber.i("Successfully loaded data.");
    }

    public void onEventMainThread(LoadDataFailureEvent event) {
        mCurrentLoadCount = 0;
        Timber.i("Failed to load data.");
    }

    public void onEventMainThread(LoadLeaderboardTimeSuccessEvent event) {
        loadDataSuccessEvent();
    }

    public void onEventMainThread(LoadLeaderboardTimeFailureEvent event) {
        loadDataFailureEvent();
    }

    public void onEventMainThread(LoadLeaderboardDistanceSuccessEvent event) {
        loadDataSuccessEvent();
    }

    public void onEventMainThread(LoadLeaderboardDistanceFailureEvent event) {
        loadDataFailureEvent();
    }

    private void loadDataSuccessEvent() {
        mCurrentLoadCount++;
        if (mCurrentLoadCount == LOAD_COUNT_TOTAL) {
            EventHelper.postEvent(new LoadDataSuccessEvent());
        }
    }

    private void loadDataFailureEvent() {
        EventHelper.postEvent(new LoadDataFailureEvent());
    }

    @Override
    public void onBackPressed() {
        updateUI();
        showQuitDialog();
    }

}