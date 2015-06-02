package com.ambergleam.android.paperplane.model;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Moon extends Entity {

    public Moon(Bitmap bitmap, Point position, Point velocity) {
        setBitmap(bitmap);
        setPosition(position);
        setVelocity(velocity);
    }

}