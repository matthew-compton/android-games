package com.ambergleam.android.paperplane;

import android.app.Application;
import android.content.Context;

import com.ambergleam.android.paperplane.util.FontUtils;

import dagger.ObjectGraph;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class BaseApplication extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDagger();
        setupTimber();
        setupCalligraphy();
    }

    private void setupDagger() {
        mObjectGraph = ObjectGraph.create(new BaseModule(this));
    }

    private void setupTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    private void setupCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(FontUtils.getTypefaceRegularPath())
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    public final void inject(Object object) {
        mObjectGraph.inject(object);
    }

}