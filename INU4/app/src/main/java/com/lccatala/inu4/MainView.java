package com.lccatala.inu4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

class Position {
    public float x, y;
}

public class MainView extends View {
    Random random = new Random();
    Paint paint = new Paint();
    Position[] circles = new Position[]{ new Position(), new Position(), new Position() };
    Position[] lineEnds = new Position[]{ new Position(), new Position()};
    int frontIndex;

    public MainView(Context context, AttributeSet attrs) {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        disableCircles();
    }

    private double distance(Position a, Position b) {
        return Math.hypot(Math.abs(b.y - a.y), Math.abs(b.x - a.x));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < 3; ++i) {
            canvas.drawCircle(circles[i].x, circles[i].y, 150.0f, paint);

            int nextCircleIndex = (i+1) % 3;
            canvas.drawLine(circles[i].x, circles[i].y, circles[nextCircleIndex].x, circles[nextCircleIndex].y, paint);
        }

        paint.setColor(Color.GREEN);
        canvas.drawLine(lineEnds[0].x, lineEnds[0].y, lineEnds[1].x, lineEnds[1].y, paint);
        paint.setColor(Color.BLUE);
    }

    private Position getMiddlePoint(Position a, Position b) {
        Position middle = new Position();
        middle.x = (a.x + b.x) / 2;
        middle.y = (a.y + b.y) / 2;
        return middle;
    }

    private void setCircles(MotionEvent event) {
        for (int i = 0; i < 3; ++i) {
            circles[i].x = event.getX(i);
            circles[i].y = event.getY(i);
        }

        // Which pair of circles are closer
        double abDistance = distance(circles[0], circles[1]);
        double bcDistance = distance(circles[1], circles[2]);
        double caDistance = distance(circles[2], circles[0]);
        if (abDistance <= bcDistance) {
            if (abDistance <= caDistance) {
                frontIndex = 2;
                lineEnds[1] = getMiddlePoint(circles[0], circles[1]);
            } else {
                frontIndex = 1;
                lineEnds[1] = getMiddlePoint(circles[0], circles[2]);
            }
        } else {
            frontIndex = 0;
            lineEnds[1] = getMiddlePoint(circles[1], circles[2]);
        }

        lineEnds[0].x = 2 * circles[frontIndex].x - lineEnds[1].x;
        lineEnds[0].y = 2 * circles[frontIndex].y  - lineEnds[1].y;
    }

    private void disableCircles() {
        for (int i = 0; i < 3; ++i) {
            circles[i].x = -1000;
            circles[i].y = -1000;
        }

        frontIndex = 0;

        for (int i = 0; i < 2; ++i) {
            lineEnds[i].x = -1;
            lineEnds[i].y = -1;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE: {
                if (event.getPointerCount() == 3)
                    setCircles(event);
                this.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (event.getPointerCount() < 3)
                    disableCircles();
                this.invalidate();
                break;
            }
        }
        return true;
    }
}
