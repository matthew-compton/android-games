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

    public boolean hasUpdates() {
        return mTimeAchievement_10_Unlocked ||
                mTimeAchievement_100_Unlocked ||
                mTimeAchievement_1000_Unlocked ||
                mTimeAchievement_10000_Unlocked ||
                mDistanceAchievement_10_Unlocked ||
                mDistanceAchievement_100_Unlocked ||
                mDistanceAchievement_1000_Unlocked ||
                mDistanceAchievement_10000_Unlocked;
    }

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
        reset();
    }

    private void reset() {
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