package com.ambergleam.android.paperplane.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.ambergleam.android.paperplane.BaseApplication;
import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.manager.DataManager;
import com.ambergleam.android.paperplane.model.AbstractEntity;
import com.ambergleam.android.paperplane.model.Plane;
import com.ambergleam.android.paperplane.util.DistanceUtils;
import com.ambergleam.android.paperplane.util.TimeUtils;
import com.ambergleam.android.paperplane.view.GameplayView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GameplayFragment extends Fragment {

    private static final int FRAME_RATE_MS = 20;

    @InjectView(R.id.fragment_gameplay_view) GameplayView mGameplayView;
    @InjectView(R.id.fragment_gameplay_time) TextView mTimeTextView;
    @InjectView(R.id.fragment_gameplay_distance) TextView mDistanceTextView;

    @Inject DataManager mDataManager;

    private Callbacks mCallbacks;
    private Handler mFrameUpdateHandler;
    private boolean mPaused;

    private int mDistance;
    private int mTime;

    public static GameplayFragment newInstance() {
        return new GameplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.get(getActivity()).inject(this);

        mFrameUpdateHandler = new Handler();
        pause();
        reset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_gameplay, container, false);
        ButterKnife.inject(this, layout);

        setupGame();
        showReadyDialog();

        // TODO - replace with actual finish condition
        mGameplayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameover();
            }
        });

        return layout;
    }

    private void setupGame() {
        Plane plane = new Plane(
                BitmapFactory.decodeResource(getResources(), R.drawable.paperplane),
                new Point(mGameplayView.getWidthHalf(), mGameplayView.getHeightHalf()),
                new Pair<>(0, 0),
                false
        );

        ArrayList<AbstractEntity> entities = new ArrayList<>();
        entities.add(plane);
        mGameplayView.setEntities(entities);
    }

    private void showReadyDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.fragment_gameplay_ready))
                .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                init();
                            }
                        });
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showGameoverDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.fragment_gameplay_gameover))
                .setMessage(getResultsString())
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mCallbacks.onGameover(mTime, mDistance);
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showPausedDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.fragment_gameplay_paused))
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        unpause();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private String getResultsString() {
        String time = getString(R.string.fragment_gameplay_gameover_time, TimeUtils.formatTime(mTime));
        String distance = getString(R.string.fragment_gameplay_gameover_distance, DistanceUtils.formatDistance(mDistance));
        return time + "\n\n" + distance;
    }

    @Override
    public void onPause() {
        super.onPause();
        mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
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

    private void reset() {
        mTime = 0;
        mDistance = 0;
    }

    synchronized private void init() {
        mPaused = false;
        mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
        mGameplayView.invalidate();
        updateUI();
        mFrameUpdateHandler.postDelayed(mFrameUpdateRunnable, FRAME_RATE_MS);
    }

    private Runnable mFrameUpdateRunnable = new Runnable() {
        @Override
        synchronized public void run() {
            if (mPaused) {
                return;
            }

            mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
            mGameplayView.invalidate();

            mDistance += 1;
            mTime += FRAME_RATE_MS;
            updateUI();

            mFrameUpdateHandler.postDelayed(mFrameUpdateRunnable, FRAME_RATE_MS);
        }
    };

    private void updateUI() {
        mTimeTextView.setText(getString(R.string.fragment_gameplay_time, TimeUtils.formatTime(mTime)));
        mDistanceTextView.setText(getString(R.string.fragment_gameplay_distance, DistanceUtils.formatDistance(mDistance)));
    }

    @OnClick(R.id.fragment_gameplay_pause)
    public void onClickPause() {
        pause();
        showPausedDialog();
    }

    private void pause() {
        mPaused = true;
    }

    private void unpause() {
        mPaused = false;
        mFrameUpdateHandler.post(mFrameUpdateRunnable);
    }

    private void gameover() {
        mPaused = true;
        showGameoverDialog();
    }

    public interface Callbacks {
        void onGameover(int time, int distance);
    }

}