package com.lccatala.inu2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

class Line {
    public float xStart = -1, yStart = -1, xEnd = -1, yEnd = -1;
    public int color = Color.BLACK;
}

public class MainView extends View {
    Random random = new Random();
    Paint paint = new Paint();
    ArrayList<Line> lines;
    Line currentLine;

    public MainView(Context context, AttributeSet attrs) {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        lines = new ArrayList<>();
        currentLine = new Line();
    }

    private void drawLine(Canvas canvas, Line line) {
        paint.setColor(line.color);
        canvas.drawLine(line.xStart, line.yStart, line.xEnd, line.yEnd, this.paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Past lines
        for (Line line : lines) {
            drawLine(canvas, line);
        }

        // Current line
        drawLine(canvas, currentLine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentLine = new Line();
                currentLine.xStart = event.getX();
                currentLine.yStart = event.getY();
                currentLine.color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                break;
            case MotionEvent.ACTION_MOVE:
                currentLine.xEnd = event.getX();
                currentLine.yEnd = event.getY();
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                lines.add(currentLine);
                this.invalidate();
                break;
        }
        return true;
    }
}
