package com.ambergleam.android.paperplane.model;

import android.graphics.Bitmap;
import android.graphics.Point;

public abstract class Entity {

    private Bitmap mBitmap;
    private Point mPosition;
    private Point mVelocity;

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
        return mPosition.x;
    }

    public int getPositionY() {
        return mPosition.y;
    }

    public int getVelocityX() {
        return mVelocity.x;
    }

    public int getVelocityY() {
        return mVelocity.y;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public void setPosition(Point position) {
        mPosition = position;
    }

    public void setVelocity(Point velocity) {
        mVelocity = velocity;
    }

    public void update() {
        mPosition.x += getVelocityX();
        mPosition.y += getVelocityY();
    }

    public boolean isColliding(Entity entity) {
        return false;
    }

}