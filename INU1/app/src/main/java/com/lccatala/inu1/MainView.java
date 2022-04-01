package com.lccatala.inu1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MainView extends View {
    Random random = new Random();
    Paint paint = new Paint();
    float xStart, yStart, xEnd, yEnd;
    int color = Color.BLACK;

    public MainView(Context context, AttributeSet attrs) {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(this.color);
        canvas.drawLine(this.xStart, this.yStart, this.xEnd, this.yEnd, this.paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xStart = event.getX();
                yStart = event.getY();
                this.color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                break;
            case MotionEvent.ACTION_MOVE:
                xEnd = event.getX();
                yEnd = event.getY();
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xStart = -1;
                yStart = -1;
                xEnd = -1;
                yEnd = -1;
                this.invalidate();
                break;
        }
        return true;
    }
}
