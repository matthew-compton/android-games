package com.ambergleam.android.paperplane.model;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Pair;

public abstract class AbstractEntity {

    private Bitmap mBitmap;
    private Point mPoint;
    private Pair<Integer, Integer> mVelocity;
    private boolean mIsAccelerating;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getWidth() {
        return mBitmap.getWidth();
    }

    public int getHeight() {
        return mBitmap.getHeight();
    }

    public int getPositionX() {
        return mPoint.x;
    }

    public int getPositionY() {
        return mPoint.y;
    }

    public int getVelocityX() {
        return mVelocity.first;
    }

    public int getVelocityY() {
        return mVelocity.second;
    }

    public boolean isAccelerating() {
        return mIsAccelerating;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public void setPoint(Point point) {
        mPoint = point;
    }

    public void setVelocity(Pair<Integer, Integer> velocity) {
        mVelocity = velocity;
    }

    public void setIsAccelerating(boolean isAccelerating) {
        mIsAccelerating = isAccelerating;
    }

}