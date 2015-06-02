package com.ambergleam.android.paperplane.model;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Plane extends Entity {

    public Plane(Bitmap bitmap, Point position, Point velocity) {
        setBitmap(bitmap);
        setPosition(position);
        setVelocity(velocity);
    }

}