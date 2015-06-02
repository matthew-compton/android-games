package com.ambergleam.android.paperplane.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ambergleam.android.paperplane.model.Entity;
import com.ambergleam.android.paperplane.model.Moon;
import com.ambergleam.android.paperplane.model.Plane;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class GameplayView extends View {

    private Paint p;
    private Plane mPlane;
    private List<Entity> mEntities;

    private int mCanvasWidth;
    private int mCanvasHeight;
    private boolean mIsSetup;

    public GameplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupListeners();
        reset();
    }

    private void setupListeners() {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Timber.i("onClick");
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

    public void reset() {
        p = new Paint();
        mIsSetup = false;
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        mCanvasWidth = canvas.getWidth();
        mCanvasHeight = canvas.getHeight();
        if (!mIsSetup && mCanvasWidth > 0 && mCanvasHeight > 0) {
            setupGame();
        } else if (mIsSetup) {
            clearScreen(canvas);
            drawSprites(canvas);
        }
    }

    private void clearScreen(Canvas canvas) {
        p.setColor(Color.WHITE);
        p.setAlpha(255);
        p.setStrokeWidth(1);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
    }

    private void drawSprites(Canvas canvas) {
        drawSprite(canvas, mPlane);
        for (Entity entity : mEntities) {
            drawSprite(canvas, entity);
        }
    }

    private void drawSprite(Canvas canvas, Entity sprite) {
        canvas.drawBitmap(sprite.getBitmap(), sprite.getPositionX(), sprite.getPositionY(), null);
    }

    private void setupGame() {
        mIsSetup = true;
        mPlane = Plane.newInstance(getContext(), mCanvasWidth, mCanvasHeight);
        mEntities = new ArrayList<>();
        mEntities.add(Moon.newInstance(getContext(), mCanvasWidth, mCanvasHeight));
        mEntities.add(Moon.newInstance(getContext(), mCanvasWidth, mCanvasHeight));
        mEntities.add(Moon.newInstance(getContext(), mCanvasWidth, mCanvasHeight));
    }

    /*
     * Return true if game continues, false if game is over
     */
    public boolean updateState() {
        mPlane.update(mCanvasWidth, mCanvasHeight);
        for (Entity entity : mEntities) {
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