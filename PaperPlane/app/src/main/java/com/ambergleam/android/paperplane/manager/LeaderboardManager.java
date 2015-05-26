package com.ambergleam.android.paperplane.manager;

import android.content.Context;

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.controller.MainActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class LeaderboardManager implements DataInterface {

    private int mBestTime;
    private int mBestDistance;
    private int mPreviousTime;
    private int mPreviousDistance;

    @Inject
    public LeaderboardManager() {
        reset();
    }

    @Override
    public void update(int time, int distance) {
        mPreviousTime = time;
        mPreviousDistance = distance;
        updateBestTime(mPreviousTime);
        updateBestDistance(mPreviousDistance);
    }

    @Override
    public void save(Context context, GoogleApiClient mGoogleApiClient) {
        Games.Leaderboards.submitScore(mGoogleApiClient, context.getString(R.string.leaderboard_id_time), mBestTime);
        Games.Leaderboards.submitScore(mGoogleApiClient, context.getString(R.string.leaderboard_id_distance), mBestDistance);
    }

    @Override
    public void load(final Context context, GoogleApiClient mGoogleApiClient) {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                mGoogleApiClient,
                context.getString(R.string.leaderboard_id_time),
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_PUBLIC)
                .setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
                    @Override
                    public void onResult(Leaderboards.LoadPlayerScoreResult result) {
                        LeaderboardScore score = result.getScore();
                        int time = (int) score.getRawScore();
                        updateBestTime(time);
                        ((MainActivity) context).updateUI();
                        Timber.i("Load Time: " + time);
                    }
                });
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                mGoogleApiClient,
                context.getString(R.string.leaderboard_id_distance),
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_PUBLIC)
                .setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
                    @Override
                    public void onResult(Leaderboards.LoadPlayerScoreResult result) {
                        LeaderboardScore score = result.getScore();
                        int distance = (int) score.getRawScore();
                        updateBestDistance(distance);
                        ((MainActivity) context).updateUI();
                        Timber.i("Load Distance: " + distance);

                    }
                });
    }

    @Override
    public void reset() {
        mBestTime = 0;
        mBestDistance = 0;
        mPreviousTime = 0;
        mPreviousDistance = 0;
    }

    private void updateBestTime(int time) {
        if (mBestTime < time) {
            mBestTime = time;
        }
    }

    private void updateBestDistance(int distance) {
        if (mBestDistance < distance) {
            mBestDistance = distance;
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