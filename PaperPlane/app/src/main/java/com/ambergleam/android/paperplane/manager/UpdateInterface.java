package com.ambergleam.android.paperplane.manager;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

public interface UpdateInterface {

    void update(int time, int distance);
    void save(Context context, GoogleApiClient mGoogleApiClient);

}