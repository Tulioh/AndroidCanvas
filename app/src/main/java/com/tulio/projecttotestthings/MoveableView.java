package com.tulio.projecttotestthings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tulio on 3/31/16.
 */
public class MoveableView extends View {
    private Rect mBodyRect;
    private Rect mTopRect;
    private Rect mBottomRect;
    private Paint mBodyPaint;
    private Paint mTopRectPaint;
    private Paint mBottomRectPaint;

    private GestureDetectorCompat mDetector;

    public MoveableView(Context context) {
        this(context, null);
    }

    public MoveableView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    private void initialize() {
        int rectWidth = 500;
        int rectHeight = 300;
        int marginLeft = 200;
        int marginTop = 200;

        mBodyRect = new Rect(marginLeft, marginTop, rectWidth, rectHeight);
        mBodyPaint = new Paint();
        mBodyPaint.setColor(Color.BLACK);
        mBodyPaint.setStyle(Paint.Style.FILL);

        rectWidth = 500;
        rectHeight = 220;
        marginLeft = 200;
        marginTop = 200;

        mTopRect = new Rect(marginLeft, marginTop, rectWidth, rectHeight);
        mTopRectPaint = new Paint();
        mTopRectPaint.setColor(Color.RED);
        mTopRectPaint.setStyle(Paint.Style.FILL);

        rectWidth = 500;
        rectHeight = 300;
        marginLeft = 200;
        marginTop = 280;

        mBottomRect = new Rect(marginLeft, marginTop, rectWidth, rectHeight);
        mBottomRectPaint = new Paint();
        mBottomRectPaint.setColor(Color.RED);
        mBottomRectPaint.setStyle(Paint.Style.FILL);

        mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(mBodyRect, mBodyPaint);
        canvas.drawRect(mTopRect, mTopRectPaint);
        canvas.drawRect(mBottomRect, mBottomRectPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private float lastValue = 0;

        @Override
        public boolean onScroll(MotionEvent startedMotion, MotionEvent endMotion, float distanceX, float distanceY) {
            switch(endMotion.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float scrolledSize = (endMotion.getY() - startedMotion.getY());
                    mBodyRect.bottom += scrolledSize - lastValue;
                    lastValue = scrolledSize;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    lastValue = 0;
                    break;
            }

            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
