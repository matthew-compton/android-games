package com.ambergleam.android.paperplane.outbox;

import android.content.Context;

import com.ambergleam.android.paperplane.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class LeaderboardOutbox implements OutboxInterface {

    public int mBestTime;
    public int mBestDistance;

    public LeaderboardOutbox() {
        reset();
    }

    @Override
    public boolean hasUpdates() {
        return mBestTime >= 0 || mBestDistance >= 0;
    }

    @Override
    public void update(int time, int distance) {
        updateBestTime(time);
        updateBestDistance(distance);
    }

    @Override
    public void save(Context context, GoogleApiClient mGoogleApiClient) {
        if (mBestTime >= 0) {
            Games.Leaderboards.submitScore(mGoogleApiClient, context.getString(R.string.leaderboard_id_time), mBestTime);
        }
        if (mBestDistance >= 0) {
            Games.Leaderboards.submitScore(mGoogleApiClient, context.getString(R.string.leaderboard_id_distance), mBestDistance);
        }
        reset();
    }

    private void reset() {
        mBestTime = -1;
        mBestDistance = -1;
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

}