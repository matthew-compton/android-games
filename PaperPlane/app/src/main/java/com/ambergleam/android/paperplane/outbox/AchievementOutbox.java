package com.ambergleam.android.paperplane.outbox;

import android.content.Context;

import com.ambergleam.android.paperplane.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class AchievementOutbox implements OutboxInterface {

    public boolean mTimeAchievement_10_Unlocked;
    public boolean mTimeAchievement_100_Unlocked;
    public boolean mTimeAchievement_1000_Unlocked;
    public boolean mTimeAchievement_10000_Unlocked;
    public boolean mDistanceAchievement_10_Unlocked;
    public boolean mDistanceAchievement_100_Unlocked;
    public boolean mDistanceAchievement_1000_Unlocked;
    public boolean mDistanceAchievement_10000_Unlocked;

    @Override
    public void update(int time, int distance) {
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
        if (distance >= 10) {
            mDistanceAchievement_10_Unlocked = true;
        }
        if (distance >= 100) {
            mDistanceAchievement_100_Unlocked = true;
        }
        if (distance >= 1000) {
            mDistanceAchievement_1000_Unlocked = true;
        }
        if (distance >= 10000) {
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

}