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

public class MenuFragment extends Fragment {

    private static final String ARG_SIGNED_IN = "MenuFragment.ARG_SIGNED_IN";
    private static final String ARG_PLAYER_NAME = "MenuFragment.ARG_PLAYER_NAME";

    @InjectView(R.id.fragment_menu_signInView) View mSignInView;
    @InjectView(R.id.fragment_menu_signOutView) View mSignOutView;
    @InjectView(R.id.fragment_menu_welcome_message) TextView mWelcomeMessageTextView;

    private Callbacks mCallbacks;
    private boolean mSignedIn;
    private String mPlayerName;

    public static MenuFragment newInstance(boolean signedIn, String playerName) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SIGNED_IN, signedIn);
        args.putString(ARG_PLAYER_NAME, playerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignedIn = getArguments().getBoolean(ARG_SIGNED_IN, false);
        mPlayerName = getArguments().getString(ARG_PLAYER_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_menu, container, false);
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

    public void setSignedIn(boolean signedIn) {
        mSignedIn = signedIn;
        updateUI();
    }

    public void setWelcomeMessage(String playerName) {
        mPlayerName = playerName;
        updateUI();
    }

    private void updateUI() {
        mSignInView.setVisibility(mSignedIn ? View.GONE : View.VISIBLE);
        mSignOutView.setVisibility(mSignedIn ? View.VISIBLE : View.GONE);
        mWelcomeMessageTextView.setText(
                mPlayerName == null ?
                        getString(R.string.fragment_menu_welcome_message_default) :
                        getString(R.string.fragment_menu_welcome_message, mPlayerName)
        );
    }

    @OnClick(R.id.fragment_menu_item_start)
    public void onClickStart() {
        mCallbacks.onStartGameRequested();
    }

    @OnClick(R.id.fragment_menu_item_achievements)
    public void onClickAchievements() {
        mCallbacks.onShowAchievementsRequested();
    }

    @OnClick(R.id.fragment_menu_item_leaderboards)
    public void onClickLeaderboards() {
        mCallbacks.onShowLeaderboardsRequested();
    }

    @OnClick(R.id.fragment_menu_item_quit)
    public void onClickQuit() {
        mCallbacks.onQuitRequested();
    }

    @OnClick(R.id.fragment_menu_sign_in)
    public void onClickSignIn() {
        mCallbacks.onSignInRequested();
    }

    @OnClick(R.id.fragment_menu_sign_out)
    public void onClickSignOut() {
        mCallbacks.onSignOutRequested();
    }

    public interface Callbacks {
        void onStartGameRequested();
        void onShowAchievementsRequested();
        void onShowLeaderboardsRequested();
        void onQuitRequested();
        void onSignInRequested();
        void onSignOutRequested();
    }

}