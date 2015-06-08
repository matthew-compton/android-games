package com.ambergleam.android.paperplane.controller;

import android.app.Activity;
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
    @InjectView(R.id.fragment_gameplay_overlay) TextView mOverlayTextView;
    @InjectView(R.id.fragment_gameplay_time) TextView mTimeTextView;
    @InjectView(R.id.fragment_gameplay_distance) TextView mDistanceTextView;
    @InjectView(R.id.fragment_gameplay_restart) ImageView mRestartImageView;
    @InjectView(R.id.fragment_gameplay_pause) ImageView mPauseImageView;
    @InjectView(R.id.fragment_gameplay_play) ImageView mPlayImageView;

    private Callbacks mCallbacks;
    private Handler mFrameUpdateHandler;
    private GameState mGameState;

    private int mDistance;
    private int mTime;

    public static GameplayFragment newInstance() {
        return new GameplayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_gameplay, container, false);
        ButterKnife.inject(this, layout);
        reset();
        toReadyState();
        return layout;
    }

    @Override
    public void onPause() {
        super.onPause();
        mFrameUpdateHandler.removeCallbacks(mFrameUpdateRunnable);
        if (mGameState == GameState.RUNNING) {
            toPauseState();
        }
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

    public GameState getGameState() {
        return mGameState;
    }

    @OnClick(R.id.fragment_gameplay_restart)
    public void onClickRestart() {
        switch (mGameState) {
            case GAMEOVER:
                reset();
                toReadyState();
                break;
            case READY:
            case PAUSED:
            case RUNNING:
            default:
                throw new IllegalStateException();
        }
    }

    @OnClick(R.id.fragment_gameplay_play)
    public void onClickPlay() {
        switch (mGameState) {
            case READY:
            case PAUSED:
                toRunningState();
                break;
            case RUNNING:
            case GAMEOVER:
            default:
                throw new IllegalStateException();
        }
    }

    @OnClick(R.id.fragment_gameplay_pause)
    public void onClickPause() {
        switch (mGameState) {
            case RUNNING:
                toPauseState();
                break;
            case READY:
            case PAUSED:
            case GAMEOVER:
            default:
                throw new IllegalStateException();
        }
    }

    @OnClick(R.id.fragment_gameplay_overlay)
    public void onClickOverlay() {
        switch (mGameState) {
            case READY:
            case PAUSED:
                toRunningState();
                break;
            case GAMEOVER:
                mCallbacks.onGameover(mTime, mDistance);
                break;
            case RUNNING:
            default:
                throw new IllegalStateException();
        }
    }

    public interface Callbacks {
        void onGameover(int time, int distance);
    }

    public void reset() {
        mFrameUpdateHandler = new Handler();
        mGameplayView.reset();
        mGameplayView.invalidate();
        mTime = 0;
        mDistance = 0;
    }

    private final Runnable mFrameUpdateRunnable = new Runnable() {
        @Override
        synchronized public void run() {
            if (mGameState != GameState.RUNNING) {
                return;
            }

            boolean gameover = !mGameplayView.updateState();
            mDistance += Math.abs(mGameplayView.getPlane().getVelocity());
            mTime += FRAME_RATE_MS;

            if (gameover) {
                toGameoverState();
            } else {
                toRunningState();
            }
        }
    };

    private void updateUI() {
        mTimeTextView.setText(getString(R.string.fragment_gameplay_time, TimeUtils.formatTime(mTime)));
        mDistanceTextView.setText(getString(R.string.fragment_gameplay_distance, DistanceUtils.formatDistance(mDistance)));
        if (mGameState == GameState.RUNNING) {
            updateRunningUI();
        } else if (mGameState == GameState.READY) {
            updateReadyUI();
        } else if (mGameState == GameState.PAUSED) {
            updatePausedUI();
        } else if (mGameState == GameState.GAMEOVER) {
            updateGameoverUI();
        } else {
            throw new IllegalStateException();
        }
        mGameplayView.invalidate();
    }

    private void updateRunningUI() {
        mRestartImageView.setVisibility(View.GONE);
        mPauseImageView.setVisibility(View.VISIBLE);
        mPlayImageView.setVisibility(View.GONE);
        mOverlayTextView.setVisibility(View.GONE);
        mOverlayTextView.setText(null);
        mGameplayView.setAlpha(1.0f);
        mGameplayView.enableListeners();
    }

    private void updateReadyUI() {
        mRestartImageView.setVisibility(View.GONE);
        mPauseImageView.setVisibility(View.GONE);
        mPlayImageView.setVisibility(View.VISIBLE);
        mOverlayTextView.setVisibility(View.VISIBLE);
        mOverlayTextView.setText(R.string.fragment_gameplay_overlay_ready);
        mGameplayView.setAlpha(1.0f);
        mGameplayView.disableListeners();
    }

    private void updatePausedUI() {
        mRestartImageView.setVisibility(View.GONE);
        mPauseImageView.setVisibility(View.GONE);
        mPlayImageView.setVisibility(View.VISIBLE);
        mOverlayTextView.setVisibility(View.VISIBLE);
        mOverlayTextView.setText(R.string.fragment_gameplay_overlay_paused);
        mGameplayView.setAlpha(0.5f);
        mGameplayView.disableListeners();
    }

    private void updateGameoverUI() {
        mRestartImageView.setVisibility(View.VISIBLE);
        mPauseImageView.setVisibility(View.GONE);
        mPlayImageView.setVisibility(View.GONE);
        mOverlayTextView.setVisibility(View.VISIBLE);
        mOverlayTextView.setText(R.string.fragment_gameplay_overlay_gameover);
        mGameplayView.setAlpha(0.5f);
        mGameplayView.disableListeners();
    }

    public void toReadyState() {
        mGameState = GameState.READY;
        updateUI();
    }

    public void toPauseState() {
        mGameState = GameState.PAUSED;
        updateUI();
    }

    public void toGameoverState() {
        mGameState = GameState.GAMEOVER;
        updateUI();
    }

    public void toRunningState() {
        mGameState = GameState.RUNNING;
        updateUI();
        mFrameUpdateHandler.postDelayed(mFrameUpdateRunnable, FRAME_RATE_MS);
    }


}