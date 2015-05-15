package com.ambergleam.android.paperplane.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ambergleam.android.paperplane.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class GameplayView extends View {

    private Paint p;
    private List<AbstractEntity> mEntities;

    private int mCanvasWidth;
    private int mCanvasHeight;

    public GameplayView(Context context, AttributeSet aSet) {
        super(context, aSet);
        p = new Paint();
        setEntities(new ArrayList<AbstractEntity>());
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        mCanvasWidth = canvas.getWidth();
        mCanvasHeight = canvas.getHeight();
        clearScreen(canvas);
        drawSprites(canvas);
    }


    private void clearScreen(Canvas canvas) {
        p.setColor(Color.WHITE);
        p.setAlpha(255);
        p.setStrokeWidth(1);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
    }

    private void drawSprites(Canvas canvas) {
        for (AbstractEntity abstractEntity : mEntities) {
            canvas.drawBitmap(abstractEntity.getBitmap(), abstractEntity.getPositionX(), abstractEntity.getPositionY(), null);
        }
    }

    public void setEntities(ArrayList<AbstractEntity> entities) {
        mEntities = entities;
    }

    public int getWidthHalf() {
        return mCanvasWidth / 2;
    }

    public int getHeightHalf() {
        return mCanvasHeight / 2;
    }

}