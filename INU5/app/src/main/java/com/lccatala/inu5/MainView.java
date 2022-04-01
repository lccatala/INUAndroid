package com.lccatala.inu5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

class Rectangle {
    public Rectangle(RectF rectNew) {
        rect = rectNew;
        color = Color.rgb(random.nextInt(), random.nextInt(), random.nextInt());
    }

    public int color;
    public float width = 400, height = 400;
    public RectF rect;

    static Random random = new Random();
}

public class MainView extends View {
    class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            super.onScale(detector);
            int idx = getSelectedSquareIndex(detector.getFocusX(), detector.getFocusY());
            if (idx == -1)
                return true;

            float scaleDelta = detector.getCurrentSpan() - detector.getPreviousSpan();
            float x = rectangles.get(idx).rect.centerX();
            float y = rectangles.get(idx).rect.centerY();
            float w = rectangles.get(idx).rect.width();
            float h = rectangles.get(idx).rect.height();
            float speed = 0.2f;
            float timeDelta = detector.getTimeDelta();
            rectangles.get(idx).rect.top = y - (h / 2) - scaleDelta * speed * timeDelta;
            rectangles.get(idx).rect.bottom = y + (h / 2) + scaleDelta * speed * timeDelta;
            rectangles.get(idx).rect.left = x - (w / 2) - scaleDelta * speed * timeDelta;
            rectangles.get(idx).rect.right = x + (w / 2) + scaleDelta * speed * timeDelta;
            return true;
        }
    }

    class SimpleGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int halfSide = 200;
            rectangles.add(new Rectangle(new RectF(
                    e.getX() - halfSide,
                    e.getY() - halfSide,
                    e.getX() + halfSide,
                    e.getY() + halfSide)));// left top right bottom
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            super.onScroll(e1, e2, distanceX, distanceY);
            int idx = getSelectedSquareIndex(e2.getX(), e2.getY());

            if (idx == -1)
                return true;

            rectangles.get(idx).rect.left -= distanceX;
            rectangles.get(idx).rect.right -= distanceX;
            rectangles.get(idx).rect.top -= distanceY;
            rectangles.get(idx).rect.bottom -= distanceY;
            return true;
        }
    }

    Paint paint = new Paint();

    GestureDetector gestureDetector;
    ScaleGestureDetector scaleDetector;
    SimpleGestureListener gestureListener;
    ScaleGestureListener scaleListener;
    ArrayList<Rectangle> rectangles = new ArrayList<>();


    public MainView(Context context, AttributeSet attrs) {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);

        gestureListener = new SimpleGestureListener();
        gestureDetector = new GestureDetector(getContext(), gestureListener);
        gestureDetector.setOnDoubleTapListener(gestureListener);

        scaleListener = new ScaleGestureListener();
        scaleDetector = new ScaleGestureDetector(getContext(), scaleListener);
    }

    public int getSelectedSquareIndex(float x, float y) {
        int idx = -1;
        for (int i = 0; i < rectangles.size(); ++i) {
            if (rectangles.get(i).rect.contains(x, y)) {
                idx = i;
                break;
            }
        }
        return idx;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        for (Rectangle r : rectangles) {
            paint.setColor(r.color);
            canvas.drawRect(r.rect, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleDetector.onTouchEvent(event);
        this.invalidate();
        return true;
    }
}
