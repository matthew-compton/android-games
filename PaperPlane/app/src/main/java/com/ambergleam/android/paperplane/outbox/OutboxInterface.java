package com.ambergleam.android.paperplane.outbox;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

public interface OutboxInterface {

    boolean hasUpdates();
    void update(int time, int distance);
    void save(Context context, GoogleApiClient mGoogleApiClient);

}