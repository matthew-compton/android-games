package com.ambergleam.android.paperplane.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambergleam.android.paperplane.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GameoverFragment extends Fragment {

    private static final String ARG_TIME = "GameoverFragment.ARG_TIME";
    private static final String ARG_DISTANCE = "GameoverFragment.ARG_DISTANCE";


    @InjectView(R.id.fragment_gameover_time) TextView mTimeTextView;
    @InjectView(R.id.fragment_gameover_distance) TextView mDistanceTextView;

    private Callbacks mCallbacks;
    private int mTime;
    private int mDistance;

    public static GameoverFragment newInstance(int time, int distance) {
        GameoverFragment fragment = new GameoverFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TIME, time);
        args.putInt(ARG_DISTANCE, distance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTime = getArguments().getInt(ARG_TIME, 0);
        mDistance = getArguments().getInt(ARG_DISTANCE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_gameover, container, false);
        ButterKnife.inject(this, layout);
        updateUI();
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

    @OnClick(R.id.fragment_gameover_menu)
    public void onClickMainMenu() {
        mCallbacks.onGameoverScreenDismissed();
    }

    private void updateUI() {
        mTimeTextView.setText(getString(R.string.fragment_gameover_time, mTime));
        mDistanceTextView.setText(getString(R.string.fragment_gameover_distance, mDistance));
    }

    public interface Callbacks {
        void onGameoverScreenDismissed();
    }

}