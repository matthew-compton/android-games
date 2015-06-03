package com.ambergleam.android.paperplane.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.util.RandomUtils;

import java.util.Random;

public class Moon extends Entity {

    public static final int MAX_VELOCITY = 8;

    public static Moon newInstance(Context context, int canvasWidth, int canvasHeight) {
        Moon moon = new Moon();
        moon.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.moon));

        Random random = new Random();
        int positionX = random.nextInt(getRandomPositionX());
        int positionY = random.nextInt(getRandomPositionY());
        int velocityX = getRandomVelocity();
        int velocityY = getRandomVelocity();

        moon.setPosition(new Point(positionX, positionY));
        moon.setVelocity(new Point(velocityX, velocityY));
        return moon;
    }

    private static int getRandomPositionX() {
        return 0;
    }

    private static int getRandomPositionY() {
        return 0;
    }

    private static int getRandomVelocity() {
        return RandomUtils.generateRandomValue(MAX_VELOCITY);
    }

}