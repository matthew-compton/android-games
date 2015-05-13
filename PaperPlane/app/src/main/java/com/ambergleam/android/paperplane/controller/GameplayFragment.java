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

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.model.AbstractEntity;
import com.ambergleam.android.paperplane.model.Plane;
import com.ambergleam.android.paperplane.view.GameplayView;

import java.util.ArrayList;

public class GameplayFragment extends Fragment implements View.OnClickListener {

    private static final int FRAME_RATE = 20;

    private Callbacks mCallbacks;
    private GameplayView mGameplayView;
    private Handler mFrame;

    private int mDistance = 0;
    private int mTime = 0;

    public static GameplayFragment newInstance() {
        return new GameplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrame = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGameplayView = (GameplayView) inflater.inflate(R.layout.fragment_gameplay, container, false);

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
        return mGameplayView;
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

    synchronized public void init() {
        mFrame.removeCallbacks(frameUpdate);
        mGameplayView.invalidate();
        mFrame.postDelayed(frameUpdate, FRAME_RATE);
    }

    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            mFrame.removeCallbacks(frameUpdate);
            mGameplayView.invalidate();

            mDistance += 2;
            mTime += 1;

            mFrame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };

    @Override
    public void onClick(View v) {
        mCallbacks.onGameover(mTime, mDistance);
    }

    public interface Callbacks {
        void onGameover(int time, int distance);
    }

}