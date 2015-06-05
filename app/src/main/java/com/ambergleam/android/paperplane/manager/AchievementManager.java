package com.ambergleam.android.paperplane.manager;

import android.content.Context;

import com.ambergleam.android.paperplane.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class AchievementManager implements DataInterface {

    private boolean mTimeAchievement_10_Unlocked;
    private boolean mTimeAchievement_100_Unlocked;
    private boolean mTimeAchievement_1000_Unlocked;
    private boolean mTimeAchievement_10000_Unlocked;
    private boolean mDistanceAchievement_10_Unlocked;
    private boolean mDistanceAchievement_100_Unlocked;
    private boolean mDistanceAchievement_1000_Unlocked;
    private boolean mDistanceAchievement_10000_Unlocked;

    @Inject
    public AchievementManager() {
        reset();
    }

    @Override
    public void update(int time_ms, int distance_ft) {
        int time = time_ms / 1000;
        if (time >= 10) {
            mTimeAchievement_10_Unlocked = true;
        }
        if (time >= 100) {
            mTimeAchievement_100_Unlocked = true;
        }
        if (time >= 1000) {
            mTimeAchievement_1000_Unlocked = true;
        }
        if (time >= 10000) {
            mTimeAchievement_10000_Unlocked = true;
        }
        if (distance_ft >= 10) {
            mDistanceAchievement_10_Unlocked = true;
        }
        if (distance_ft >= 100) {
            mDistanceAchievement_100_Unlocked = true;
        }
        if (distance_ft >= 1000) {
            mDistanceAchievement_1000_Unlocked = true;
        }
        if (distance_ft >= 10000) {
            mDistanceAchievement_10000_Unlocked = true;
        }
    }

    @Override
    public void save(Context context, GoogleApiClient mGoogleApiClient) {
        if (mTimeAchievement_10_Unlocked) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.achievement_id_time_10));
        }
        if (mTimeAchievement_100_Unlocked) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.achievement_id_time_100));
        }
        if (mTimeAchievement_1000_Unlocked) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.achievement_id_time_1000));
        }
        if (mTimeAchievement_10000_Unlocked) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.achievement_id_time_10000));
        }
        if (mDistanceAchievement_10_Unlocked) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.achievement_id_distance_10));
        }
        if (mDistanceAchievement_100_Unlocked) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.achievement_id_distance_100));
        }
        if (mDistanceAchievement_1000_Unlocked) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.achievement_id_distance_1000));
        }
        if (mDistanceAchievement_10000_Unlocked) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.achievement_id_distance_10000));
        }
    }

    @Override
    public void load(Context context, GoogleApiClient mGoogleApiClient) {
        Timber.d("Achievements not loaded.");
    }

    @Override
    public void reset() {
        mTimeAchievement_10_Unlocked = false;
        mTimeAchievement_100_Unlocked = false;
        mTimeAchievement_1000_Unlocked = false;
        mTimeAchievement_10000_Unlocked = false;
        mDistanceAchievement_10_Unlocked = false;
        mDistanceAchievement_100_Unlocked = false;
        mDistanceAchievement_1000_Unlocked = false;
        mDistanceAchievement_10000_Unlocked = false;
    }

}