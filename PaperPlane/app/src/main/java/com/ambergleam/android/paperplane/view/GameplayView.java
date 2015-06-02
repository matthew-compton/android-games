package com.ambergleam.android.paperplane.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.ambergleam.android.paperplane.R;
import com.ambergleam.android.paperplane.model.Entity;
import com.ambergleam.android.paperplane.model.Plane;

import java.util.ArrayList;
import java.util.List;

public class GameplayView extends View {

    private Paint p;
    private Plane mPlane;
    private List<Entity> mEntities;

    private int mCanvasWidth;
    private int mCanvasHeight;
    private boolean mIsSetup;

    public GameplayView(Context context, AttributeSet aSet) {
        super(context, aSet);
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
        mPlane = new Plane(
                BitmapFactory.decodeResource(getResources(), R.drawable.paperplane),
                new Point(0, mCanvasHeight / 2),
                new Point(1, 0)
        );
        mEntities = new ArrayList<>();
        mEntities.add(new Plane(
                BitmapFactory.decodeResource(getResources(), R.drawable.moon),
                new Point(mCanvasWidth / 2, 0),
                new Point(2, 0)
        ));
        mEntities.add(new Plane(
                BitmapFactory.decodeResource(getResources(), R.drawable.moon),
                new Point(mCanvasWidth / 2, mCanvasHeight / 2),
                new Point(4, 0)
        ));
        mEntities.add(new Plane(
                BitmapFactory.decodeResource(getResources(), R.drawable.moon),
                new Point(mCanvasWidth / 2, mCanvasHeight),
                new Point(8, 0)
        ));
    }

    /*
     * Return true if game continues, false if game is over
     */
    public boolean updateState() {
        mPlane.update();
        for (Entity entity : mEntities) {
            entity.update();
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