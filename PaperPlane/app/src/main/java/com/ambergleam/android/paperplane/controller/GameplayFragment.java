package com.ambergleam.android.paperplane.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.model.GameState;
import com.ambergleam.android.paperplane.util.DistanceUtils;
import com.ambergleam.android.paperplane.util.TimeUtils;
import com.ambergleam.android.paperplane.view.GameplayView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GameplayFragment extends Fragment {

    private static final int FRAME_RATE_MS = 20;

    @InjectView(R.id.fragment_gameplay_view) GameplayView mGameplayView;
    @InjectView(R.id.fragment_gameplay_time) TextView mTimeTextView;
    @InjectView(R.id.fragment_gameplay_distance) TextView mDistanceTextView;
    @InjectView(R.id.fragment_gameplay_pause) ImageView mPauseImageView;
    @InjectView(R.id.fragment_gameplay_unpause) ImageView mUnpauseImageView;

    private Callbacks mCallbacks;
    private Handler mFrameUpdateHandler;
    private GameState mGameState;

    private int mDistance;
    private int mTime;

    public static GameplayFragment newInstance() {
        return new GameplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_gameplay, container, false);
        ButterKnife.inject(this, layout);

        mGameplayView.setupGame();
        showReadyDialog();

        return layout;
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
        mFrameUpdateHandler = new Handler();
        mGameState = null;
        mTime = 0;
        mDistance = 0;
    }

    synchronized private void init() {
        mGameState = GameState.RUNNING;
        mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
        mGameplayView.invalidate();
        updateUI();
        mFrameUpdateHandler.postDelayed(mFrameUpdateRunnable, FRAME_RATE_MS);
    }

    private Runnable mFrameUpdateRunnable = new Runnable() {
        @Override
        synchronized public void run() {
            if (mGameState == GameState.PAUSED) {
                return;
            }

            mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
            mGameplayView.invalidate();

            if (!mGameplayView.updateState()) {
                gameover();
            }

            mDistance += mGameplayView.getPlane().getVelocityX();
            mTime += FRAME_RATE_MS;
            updateUI();

            mFrameUpdateHandler.postDelayed(mFrameUpdateRunnable, FRAME_RATE_MS);
        }
    };

    private void updateUI() {
        mTimeTextView.setText(getString(R.string.fragment_gameplay_time, TimeUtils.formatTime(mTime)));
        mDistanceTextView.setText(getString(R.string.fragment_gameplay_distance, DistanceUtils.formatDistance(mDistance)));
        switch (mGameState) {
            case RUNNING:
                mPauseImageView.setVisibility(View.VISIBLE);
                mUnpauseImageView.setVisibility(View.GONE);
                break;
            case PAUSED:
                mPauseImageView.setVisibility(View.GONE);
                mUnpauseImageView.setVisibility(View.VISIBLE);
                break;
            default:
                mPauseImageView.setVisibility(View.GONE);
                mUnpauseImageView.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick(R.id.fragment_gameplay_pause)
    public void onClickPause() {
        pause();
    }

    @OnClick(R.id.fragment_gameplay_unpause)
    public void onClickUnpause() {
        unpause();
    }

    public GameState getGameState() {
        return mGameState;
    }

    public void pause() {
        mGameState = GameState.PAUSED;
        updateUI();
    }

    public void unpause() {
        mGameState = GameState.RUNNING;
        updateUI();
        mFrameUpdateHandler.post(mFrameUpdateRunnable);
    }

    private void gameover() {
        mGameState = null;
        showGameoverDialog();
    }

    public interface Callbacks {
        void onGameover(int time, int distance);
    }

}