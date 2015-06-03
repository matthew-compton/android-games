package com.ambergleam.android.paperplane.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.ambergleam.android.paperplane.R;

public class Sun extends Enemy {

    public static Sun newInstance(Context context, int canvasWidth, int canvasHeight) {
        Sun sun = new Sun();
        sun.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sun));
        sun.setPosition(getRandomStartingPosition(canvasWidth, canvasHeight));
        sun.setVelocity(getRandomStartingVelocity());
        return sun;
    }

}