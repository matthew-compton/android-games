package com.ambergleam.android.paperplane.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.ambergleam.android.paperplane.R;

public class Moon extends Enemy {

    public static Moon newInstance(Context context, int canvasWidth, int canvasHeight) {
        Moon moon = new Moon();
        moon.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.moon));
        moon.setPosition(getRandomStartingPosition(canvasWidth, canvasHeight));
        moon.setVelocity(getRandomStartingVelocity());
        return moon;
    }

}