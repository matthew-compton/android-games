package com.ambergleam.android.paperplane.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.ambergleam.android.paperplane.R;

public class Plane extends Entity {

    public static Plane newInstance(Context context, int canvasWidth, int canvasHeight) {
        Plane plane = new Plane();
        plane.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.paperplane));
        plane.setPosition(new Point(canvasWidth / 2, canvasHeight / 2));
        plane.setVelocity(new Point(0, 0));
        return plane;
    }

}