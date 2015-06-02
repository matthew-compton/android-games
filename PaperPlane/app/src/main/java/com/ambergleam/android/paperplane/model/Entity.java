package com.ambergleam.android.paperplane.model;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

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

    public int getVelocity() {
        return (int) Math.sqrt((mVelocity.x * mVelocity.x) + (mVelocity.y * mVelocity.y));
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

    public void update(int maxWidth, int maxHeight) {
        mPosition.x = (mPosition.x + getVelocityX()) % maxWidth;
        mPosition.y = (mPosition.y + getVelocityY()) % maxHeight;
    }

    public boolean isColliding(Entity entity) {
        Rect rect1 = new Rect();
        rect1.set(this.getPositionX(), this.getPositionY(), this.getPositionX() + this.getWidth(), this.getPositionY() + this.getHeight());

        Rect rect2 = new Rect();
        rect2.set(entity.getPositionX(), entity.getPositionY(), entity.getPositionX() + entity.getWidth(), entity.getPositionY() + entity.getHeight());

        if (Rect.intersects(rect1, rect2)) {
            return true;
        }
        return false;
    }

}