package com.lccatala.inu3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

class Line {
    public Line(float xs, float ys, float xe, float ye) {
        xStart = xs;
        yStart = ys;
        xEnd = xe;
        yEnd = ye;
    }
    public float xStart = -1, yStart = -1, xEnd = -1, yEnd = -1;
    public int color = Color.BLACK;
}

class MotionData {
    public float x, y;
    public int id;
}

class PreviousPosition {
    public PreviousPosition(float newX, float newY) {
        x = newX;
        y = newY;
    }
    public float x, y;
}

public class MainView extends View {
    Random random = new Random();
    Paint paint = new Paint();
    HashMap<Integer, ArrayList<Line>> lines;
    ArrayList<Integer> pointerIDs;
    HashMap<Integer, Integer> lineColors;
    HashMap<Integer, PreviousPosition> prevPositions;

    public MainView(Context context, AttributeSet attrs) {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        lines = new HashMap<Integer, ArrayList<Line>>();
        pointerIDs = new ArrayList<Integer>();
        lineColors = new HashMap<Integer, Integer>();
        prevPositions = new HashMap<Integer, PreviousPosition>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Map.Entry<Integer, ArrayList<Line>> multiline : lines.entrySet()) {
            int id = multiline.getKey();
            int color = Objects.requireNonNull(lineColors.get(id));
            paint.setColor(color);
            for (Line line : multiline.getValue()) {
                canvas.drawLine(line.xStart, line.yStart, line.xEnd, line.yEnd, this.paint);
            }
        }
    }

    private MotionData getMotionData(MotionEvent event) {
        MotionData data = new MotionData();

        int index = event.getActionIndex();
        data.id = event.getPointerId(index);

        data.x = event.getX(index);
        data.y = event.getY(index);

        return data;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                MotionData data = getMotionData(event);

                ArrayList<Line> multiline = new ArrayList<Line>();
                multiline.add(new Line(data.x, data.y, data.x, data.y));
                lines.put(data.id, multiline);
                prevPositions.put(data.id, new PreviousPosition(data.x, data.y));
                lineColors.put(data.id, Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int i = 0; i < event.getPointerCount(); ++i) {
                    int id = event.getPointerId(i);
                    float x = event.getX(i);
                    float y = event.getY(i);
                    PreviousPosition p = Objects.requireNonNull(prevPositions.get(id));
                    if (i != 0)
                        System.out.println();
                    Objects.requireNonNull(lines.get(id)).add(new Line(p.x, p.y, x, y));
                    Objects.requireNonNull(prevPositions.get(id)).x = x;
                    Objects.requireNonNull(prevPositions.get(id)).y = y;
                }
                this.invalidate();
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP: {
                int id = event.getPointerId(event.getActionIndex());
                lines.remove(id);
                lineColors.remove(id);
                prevPositions.remove(id);
                this.invalidate();
                break;
            }
        }
        return true;
    }
}
