package com.ambergleam.android.paperplane.model;

import android.graphics.Point;

import com.ambergleam.android.paperplane.util.RandomUtils;

public abstract class Enemy extends Entity {

    protected static final int MAX_VELOCITY = 4;

    protected static Point getRandomStartingPosition(int canvasWidth, int canvasHeight) {
        return new Point(
                0,
                0
        );
    }

    protected static Point getRandomStartingVelocity() {
        return new Point(
                RandomUtils.generateRandomValue(MAX_VELOCITY),
                RandomUtils.generateRandomValue(MAX_VELOCITY)
        );
    }

}