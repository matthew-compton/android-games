package com.ambergleam.android.paperplane.controller;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.model.AbstractEntity;
import com.ambergleam.android.paperplane.model.Plane;
import com.ambergleam.android.paperplane.view.GameplayView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GameplayFragment extends Fragment {

    private static final int FRAME_RATE_MS = 20;

    @InjectView(R.id.fragment_gameplay_view) GameplayView mGameplayView;
    @InjectView(R.id.fragment_gameplay_time) TextView mTimeTextView;
    @InjectView(R.id.fragment_gameplay_distance) TextView mDistanceTextView;

    private Callbacks mCallbacks;
    private Handler mFrameUpdateHandler;

    private int mDistance;
    private int mTime;

    public static GameplayFragment newInstance() {
        return new GameplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrameUpdateHandler = new Handler();
        reset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_gameplay, container, false);
        ButterKnife.inject(this, layout);

        Plane plane = new Plane(
                BitmapFactory.decodeResource(getResources(), R.drawable.paperplane),
                new Point(mGameplayView.getWidthHalf(), mGameplayView.getHeightHalf()),
                new Pair<>(0, 0),
                false
        );

        ArrayList<AbstractEntity> entities = new ArrayList<>();
        entities.add(plane);
        mGameplayView.setEntities(entities);

        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });

        mGameplayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onGameover(mTime, mDistance);
            }
        });

        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
    }

    private void reset() {
        mTime = 0;
        mDistance = 0;
    }

    synchronized public void init() {
        mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
        mGameplayView.invalidate();
        updateUI();
        mFrameUpdateHandler.postDelayed(mFrameUpdateRunnable, FRAME_RATE_MS);
    }

    private Runnable mFrameUpdateRunnable = new Runnable() {
        @Override
        synchronized public void run() {
            mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
            mGameplayView.invalidate();

            mDistance += 1;
            mTime += FRAME_RATE_MS;
            updateUI();

            mFrameUpdateHandler.postDelayed(mFrameUpdateRunnable, FRAME_RATE_MS);
        }
    };

    private void updateUI() {
        mTimeTextView.setText(getString(R.string.fragment_gameplay_time, mTime));
        mDistanceTextView.setText(getString(R.string.fragment_gameplay_distance, mDistance));
    }

    public interface Callbacks {
        void onGameover(int time, int distance);
    }

}