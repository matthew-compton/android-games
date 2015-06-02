package com.ambergleam.android.paperplane;

import com.ambergleam.android.paperplane.controller.GameplayFragment;
import com.ambergleam.android.paperplane.controller.MainActivity;
import com.ambergleam.android.paperplane.controller.MenuFragment;
import com.ambergleam.android.paperplane.manager.AchievementManager;
import com.ambergleam.android.paperplane.manager.DataManager;
import com.ambergleam.android.paperplane.manager.LeaderboardManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                MenuFragment.class,
                GameplayFragment.class
        },
        complete = true)
public class BaseModule {

    private final BaseApplication mApplication;

    public BaseModule(BaseApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public DataManager provideDataManager(LeaderboardManager leaderboardManager, AchievementManager achievementManager) {
        return new DataManager(leaderboardManager, achievementManager);
    }

    @Provides
    @Singleton
    public LeaderboardManager provideLeaderboardManager() {
        return new LeaderboardManager();
    }

    @Provides
    @Singleton
    public AchievementManager provideAchievementManager() {
        return new AchievementManager();
    }

}