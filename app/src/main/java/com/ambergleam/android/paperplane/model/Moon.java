package com.ambergleam.android.paperplane.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.util.RandomUtils;

public class Moon extends Entity {

    public static final int MAX_VELOCITY = 4;

    public static Moon newInstance(Context context, int canvasWidth, int canvasHeight) {
        Moon moon = new Moon();
        moon.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.moon));
        moon.setPosition(getRandomStartingPosition());
        moon.setVelocity(getRandomStartingPosition());//TODO
        return moon;
    }

    private static Point getRandomStartingPosition() {
        return new Point(
                0,
                0
        );
    }

    private static Point getRandomStartingVelocity() {
        return new Point(
                RandomUtils.generateRandomValue(MAX_VELOCITY),
                RandomUtils.generateRandomValue(MAX_VELOCITY)
        );
    }

}