package com.ambergleam.android.paperplane.outbox;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

public class DataOutbox implements OutboxInterface {

    private LeaderboardOutbox mLeaderboardOutbox;
    private AchievementOutbox mAchievementOutbox;

    public DataOutbox() {
        mLeaderboardOutbox = new LeaderboardOutbox();
        mAchievementOutbox = new AchievementOutbox();
    }

    @Override
    public void update(int time, int distance) {
        mLeaderboardOutbox.update(time, distance);
        mAchievementOutbox.update(time, distance);
    }

    @Override
    public void save(Context context, GoogleApiClient mGoogleApiClient) {
        mLeaderboardOutbox.save(context, mGoogleApiClient);
        mAchievementOutbox.save(context, mGoogleApiClient);
    }

}