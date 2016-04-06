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
    private enum RectType {
        UP, DOWN
    }

    private final int MIN_BODY_HEIGHT = 100;

    private Rect mBodyRect;
    private Rect mResizableTopRect;
    private Rect mResizableBottomRect;
    private Paint mBodyPaint;
    private Paint mResizableTopRectPaint;
    private Paint mResizableBottomRectPaint;

    private GestureDetectorCompat mDetector;
    private boolean showResizableRect = false;

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

        mResizableTopRect = new Rect(marginLeft, marginTop, rectWidth, rectHeight);
        mResizableTopRectPaint = new Paint();
        mResizableTopRectPaint.setColor(Color.RED);
        mResizableTopRectPaint.setStyle(Paint.Style.FILL);

        rectWidth = 500;
        rectHeight = 300;
        marginLeft = 200;
        marginTop = 280;

        mResizableBottomRect = new Rect(marginLeft, marginTop, rectWidth, rectHeight);
        mResizableBottomRectPaint = new Paint();
        mResizableBottomRectPaint.setColor(Color.RED);
        mResizableBottomRectPaint.setStyle(Paint.Style.FILL);

        mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(mBodyRect, mBodyPaint);

        if(showResizableRect) {
            canvas.drawRect(mResizableTopRect, mResizableTopRectPaint);
            canvas.drawRect(mResizableBottomRect, mResizableBottomRectPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private TouchedRect touchedRect;

        @Override
        public boolean onDown(MotionEvent event) {
            if(showResizableRect && !isTouchInsideARect(event, mBodyRect)) {
                showResizableRect = false;
                invalidate();

                return false;
            }

            touchedRect = getTouchedRect(event);

            if(touchedRect == null) {
                return false;
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if(!showResizableRect && isTouchInsideARect(event, mBodyRect)) {
                showResizableRect = true;
                invalidate();
            }
        }

        @Override
        public boolean onScroll(MotionEvent startedMotion, MotionEvent endMotion, float distanceX, float distanceY) {
            if(endMotion.getAction() == MotionEvent.ACTION_MOVE && touchedRect != null) {
                if(touchedRect.rectType == RectType.UP) {
                    mBodyRect.top -= distanceY;
                } else {
                    mBodyRect.bottom -= distanceY;
                }

                int touchedRectHeight = touchedRect.rect.height();
                touchedRect.rect.top -= distanceY;
                touchedRect.rect.bottom = touchedRect.rect.top + touchedRectHeight;

                invalidate();
            }

            return true;
        }

        private TouchedRect getTouchedRect(MotionEvent event) {
            if(isTouchInsideARect(event, mResizableTopRect)) {
                return new TouchedRect(mResizableTopRect, RectType.UP);
            } else if(isTouchInsideARect(event, mResizableBottomRect)) {
                return new TouchedRect(mResizableBottomRect, RectType.DOWN);
            } else {
                return null;
            }
        }

        private boolean isTouchInsideARect(MotionEvent event, Rect rect) {
            return (event.getX() >= rect.left && event.getX() <= rect.right) &&
                    (event.getY() >= rect.top && event.getY() <= rect.bottom);
        }

        private class TouchedRect {
            public Rect rect;
            public RectType rectType;

            public TouchedRect(Rect rect, RectType rectType) {
                this.rect = rect;
                this.rectType = rectType;
            }
        }
    }
}
