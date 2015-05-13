package com.ambergleam.android.paperplane.model;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Pair;

public class Plane extends AbstractEntity {

    public Plane(Bitmap bitmap, Point point, Pair<Integer, Integer> velocity, boolean isAccelerating) {
        setBitmap(bitmap);
        setPoint(point);
        setVelocity(velocity);
        setIsAccelerating(isAccelerating);
    }

}