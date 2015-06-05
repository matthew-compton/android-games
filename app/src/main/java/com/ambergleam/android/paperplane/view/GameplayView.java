package com.ambergleam.android.paperplane.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.model.Enemy;
import com.ambergleam.android.paperplane.model.Entity;
import com.ambergleam.android.paperplane.model.Moon;
import com.ambergleam.android.paperplane.model.Plane;
import com.ambergleam.android.paperplane.model.Planet;
import com.ambergleam.android.paperplane.model.Sun;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class GameplayView extends View {

    private Plane mPlane;
    private List<Enemy> mEnemies;

    private int mCanvasWidth;
    private int mCanvasHeight;
    private boolean mIsSetup;
    private Bitmap mBackground;

    public GameplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        reset();
    }

    public void enableListeners() {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Timber.i("onClick");
                mPlane.resetVelocity();
            }
        });
        setOnTouchListener(new OnSwipeTouchListener() {
            public boolean onSwipeTop() {
                Timber.i("onSwipeTop");
                mPlane.updateVelocity(0, -Plane.SWIPE_VELOCITY_DELTA);
                return true;
            }

            public boolean onSwipeRight() {
                Timber.i("onSwipeRight");
                mPlane.updateVelocity(Plane.SWIPE_VELOCITY_DELTA, 0);
                return true;
            }

            public boolean onSwipeLeft() {
                Timber.i("onSwipeLeft");
                mPlane.updateVelocity(-Plane.SWIPE_VELOCITY_DELTA, 0);
                return true;
            }

            public boolean onSwipeBottom() {
                Timber.i("onSwipeBottom");
                mPlane.updateVelocity(0, Plane.SWIPE_VELOCITY_DELTA);
                return true;
            }
        });
    }

    public void disableListeners() {
        setOnClickListener(null);
        setOnTouchListener(null);
    }

    public void reset() {
        mIsSetup = false;
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        mCanvasWidth = canvas.getWidth();
        mCanvasHeight = canvas.getHeight();
        if (!mIsSetup && mCanvasWidth > 0 && mCanvasHeight > 0) {
            setupGame();
            clearScreen(canvas);
        } else if (mIsSetup) {
            clearScreen(canvas);
            drawSprites(canvas);
        }
    }

    private void clearScreen(Canvas canvas) {
        canvas.drawBitmap(mBackground, 0, 0, null);
    }

    private void drawSprites(Canvas canvas) {
        drawSprite(canvas, mPlane);
        for (Enemy enemy : mEnemies) {
            drawSprite(canvas, enemy);
        }
    }

    private void drawSprite(Canvas canvas, Entity sprite) {
        canvas.drawBitmap(sprite.getBitmap(), sprite.getPositionX(), sprite.getPositionY(), null);
    }

    private void setupGame() {
        mIsSetup = true;

        // Background
        mBackground = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.space),
                mCanvasWidth,
                mCanvasHeight,
                true
        );

        // Player
        mPlane = Plane.newInstance(getContext(), mCanvasWidth, mCanvasHeight);

        // Enemies
        mEnemies = new ArrayList<>();
        mEnemies.add(Sun.newInstance(getContext(), mCanvasWidth, mCanvasHeight));
        mEnemies.add(Planet.newInstance(getContext(), mCanvasWidth, mCanvasHeight));
        mEnemies.add(Moon.newInstance(getContext(), mCanvasWidth, mCanvasHeight));
    }

    /*
     * Return true if game continues, false if game is over
     */
    public boolean updateState() {
        mPlane.update(mCanvasWidth, mCanvasHeight);
        for (Entity entity : mEnemies) {
            entity.update(mCanvasWidth, mCanvasHeight);
            if (mPlane.isColliding(entity)) {
                return false;
            }
        }
        return true;
    }

    public Plane getPlane() {
        return mPlane;
    }

}