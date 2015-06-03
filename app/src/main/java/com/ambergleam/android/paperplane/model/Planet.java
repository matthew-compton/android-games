package com.ambergleam.android.paperplane.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.ambergleam.android.paperplane.R;

public class Planet extends Enemy {

    public static Planet newInstance(Context context, int canvasWidth, int canvasHeight) {
        Planet planet = new Planet();
        planet.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.planet));
        planet.setPosition(getRandomStartingPosition(canvasWidth, canvasHeight));
        planet.setVelocity(getRandomStartingVelocity());
        return planet;
    }

}