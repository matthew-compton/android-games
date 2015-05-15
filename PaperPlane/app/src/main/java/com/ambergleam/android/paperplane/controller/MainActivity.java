package com.ambergleam.android.paperplane.controller;

import android.app.ActivityManager;
import android.content.Context;
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

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.outbox.DataOutbox;
import com.ambergleam.android.paperplane.util.DialogUtils;
import com.ambergleam.android.paperplane.util.GameUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.plus.Plus;

import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends FragmentActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MenuFragment.Callbacks,
        GameplayFragment.Callbacks,
        GameoverFragment.Callbacks {

    private static final int REQUEST_CODE_SIGNIN = 0;
    private static final int REQUEST_CODE_UNUSED = 1;

    private GoogleApiClient mGoogleApiClient;
    private DataOutbox mOutbox;

    private boolean mResolvingConnectionFailure;
    private boolean mSignInClicked;
    private boolean mAutoStartSignInFlow;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

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
        mOutbox = new DataOutbox();
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
            ft.replace(R.id.activity_main_container, fragment);
            ft.commit();
        } else {
            Timber.e("Error in creating fragment.");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Timber.d("Connected to Google APIs");
        updateUI();
        saveData();
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

    @Override
    public void onQuitRequested() {
        finish();
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
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        updateUI();
    }

    @Override
    public void onGameoverScreenDismissed() {
        updateFragment(MenuFragment.newInstance(isSignedIn(), getPlayerName()));
    }

    @Override
    public void onGameover(int time, int distance) {
        mOutbox.update(time, distance);
        saveData();
        updateFragment(GameoverFragment.newInstance(time, distance));
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
        } else if (fragment instanceof GameplayFragment) {
            // Nothing
        } else if (fragment instanceof GameoverFragment) {
            // Nothing
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
            mOutbox.save(this, mGoogleApiClient);
        } else {
            // TODO - save local
        }
    }

}