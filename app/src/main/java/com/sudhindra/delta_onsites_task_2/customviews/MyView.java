package com.sudhindra.delta_onsites_task_2.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sudhindra.delta_onsites_task_2.R;

public class MyView extends View {

    private static final int NO_GROUP = 0, GROUP_1 = 1, GROUP_2 = 2;
    private static final int NO_POINT = -1, DRAG_POINT = -2;

    private Point[] cornerPoints;
    private Point dragPoint;
    private Paint paint;
    private Bitmap pointBitmap;

    private int pointGroup = -1;
    private int pointIndex = 0;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        dragPoint = new Point();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Color.parseColor("#6200EE"));

        pointBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.point);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int a = 150;
        int cX = w / 2, cY = h / 2;
        cornerPoints = new Point[4];
        cornerPoints[0] = new Point();
        cornerPoints[1] = new Point();
        cornerPoints[2] = new Point();
        cornerPoints[3] = new Point();

        cornerPoints[0].x = cX - a;
        cornerPoints[0].y = cY - a;
        cornerPoints[1].x = cX + a;
        cornerPoints[1].y = cY - a;
        cornerPoints[2].x = cX + a;
        cornerPoints[2].y = cY + a;
        cornerPoints[3].x = cX - a;
        cornerPoints[3].y = cY + a;

        dragPoint.x = (cornerPoints[0].x + cornerPoints[1].x) / 2;
        dragPoint.y = (cornerPoints[0].y + cornerPoints[3].y) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(cornerPoints[0].x, cornerPoints[1].y, cornerPoints[2].x, cornerPoints[3].y, paint);
        for (int i = 0; i < 4; i++)
            drawPointBitmap(canvas, cornerPoints[i].x, cornerPoints[i].y);
        drawPointBitmap(canvas, dragPoint.x, dragPoint.y);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointIndex = NO_POINT;
                pointGroup = NO_GROUP;
                for (int i = 0; i < 4; i++) {
                    int cX = cornerPoints[i].x;
                    int cY = cornerPoints[i].y;
                    double dist = Math.sqrt((double) (((cX - x) * (cX - x)) + (cY - y) * (cY - y)));

                    if (dist - 100 < (float) pointBitmap.getWidth() / 2) {
                        pointIndex = i;
                        if (pointIndex == 0 || pointIndex == 2) pointGroup = GROUP_1;
                        else pointGroup = GROUP_2;
                        break;
                    }
                }

                int cX = dragPoint.x;
                int cY = dragPoint.y;
                double dist = Math.sqrt((double) (((cX - x) * (cX - x)) + (cY - y) * (cY - y)));

                if (dist - 100 < (float) pointBitmap.getWidth() / 2)
                    pointIndex = DRAG_POINT;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointIndex != NO_POINT && pointIndex != DRAG_POINT) {
                    cornerPoints[pointIndex].x = x;
                    cornerPoints[pointIndex].y = y;
                    if (pointGroup == GROUP_1) {
                        cornerPoints[1].x = cornerPoints[2].x;
                        cornerPoints[1].y = cornerPoints[0].y;
                        cornerPoints[3].x = cornerPoints[0].x;
                        cornerPoints[3].y = cornerPoints[2].y;
                    } else if (pointGroup == GROUP_2) {
                        cornerPoints[0].x = cornerPoints[3].x;
                        cornerPoints[0].y = cornerPoints[1].y;
                        cornerPoints[2].x = cornerPoints[1].x;
                        cornerPoints[2].y = cornerPoints[3].y;
                    }
                    dragPoint.x = (cornerPoints[0].x + cornerPoints[1].x) / 2;
                    dragPoint.y = (cornerPoints[0].y + cornerPoints[3].y) / 2;
                    invalidate();
                } else if (pointIndex == DRAG_POINT) {
                    int dx = x - dragPoint.x;
                    int dy = y - dragPoint.y;
                    for (int i = 0; i < 4; i++) {
                        cornerPoints[i].x += dx;
                        cornerPoints[i].y += dy;
                    }
                    dragPoint.x = x;
                    dragPoint.y = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void drawPointBitmap(Canvas canvas, int x, int y) {
        canvas.drawBitmap(pointBitmap, x - (float) pointBitmap.getWidth() / 2, y - (float) pointBitmap.getHeight() / 2, null);
    }
}
