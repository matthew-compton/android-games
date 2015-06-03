package com.ambergleam.android.paperplane.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.ambergleam.android.paperplane.R;

public class Plane extends Entity {

    public static final int MAX_VELOCITY = 16;
    public static final int SWIPE_VELOCITY_DELTA = 4;

    public static Plane newInstance(Context context, int canvasWidth, int canvasHeight) {
        Plane plane = new Plane();
        plane.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.paperplane));
        plane.setPosition(getStartingPosition(canvasWidth, canvasHeight, plane.getBitmap().getWidth(), plane.getBitmap().getHeight()));
        plane.setVelocity(getStartingVelocity());
        return plane;
    }

    public void updateVelocity(int dx, int dy) {
        int vx = getVelocityX() + dx;
        if (vx > MAX_VELOCITY) {
            vx = MAX_VELOCITY;
        } else if (vx < -MAX_VELOCITY) {
            vx = -MAX_VELOCITY;
        }
        int vy = getVelocityY() + dy;
        if (vy > MAX_VELOCITY) {
            vy = MAX_VELOCITY;
        } else if (vy < -MAX_VELOCITY) {
            vy = -MAX_VELOCITY;
        }
        setVelocity(new Point(vx, vy));
    }

    private static Point getStartingPosition(int canvasWidth, int canvasHeight, int bitmapWidth, int bitmapHeight) {
        int x = canvasWidth / 2 - bitmapWidth / 2;
        int y = canvasHeight / 2 - bitmapHeight / 2;
        return new Point(x, y);
    }

    private static Point getStartingVelocity() {
        return new Point(0, 0);
    }

}