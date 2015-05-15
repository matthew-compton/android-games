package com.ambergleam.android.paperplane.manager;

import android.content.Context;

import com.ambergleam.android.paperplane.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LeaderboardManager implements UpdateInterface {

    private int mBestTime;
    private int mBestDistance;
    private int mPreviousTime;
    private int mPreviousDistance;

    @Inject
    public LeaderboardManager() {

    }

    @Override
    public void update(int time, int distance) {
        mPreviousTime = time;
        mPreviousDistance = distance;
        updateBestTime();
        updateBestDistance();
    }

    @Override
    public void save(Context context, GoogleApiClient mGoogleApiClient) {
        Games.Leaderboards.submitScore(mGoogleApiClient, context.getString(R.string.leaderboard_id_time), mBestTime);
        Games.Leaderboards.submitScore(mGoogleApiClient, context.getString(R.string.leaderboard_id_distance), mBestDistance);
    }

    private void updateBestTime() {
        if (mBestTime < mPreviousTime) {
            mBestTime = mPreviousTime;
        }
    }

    private void updateBestDistance() {
        if (mBestDistance < mPreviousDistance) {
            mBestDistance = mPreviousDistance;
        }
    }

    public int getBestTime() {
        return mBestTime;
    }

    public int getBestDistance() {
        return mBestDistance;
    }

    public int getPreviousTime() {
        return mPreviousTime;
    }

    public int getPreviousDistance() {
        return mPreviousDistance;
    }

}