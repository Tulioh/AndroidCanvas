package com.tulio.projecttotestthings;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tulio on 3/31/16.
 */
public class MoveableView extends View {
    private enum RectType {
        TOP, BOTTOM
    }

    private final int MIN_BODY_HEIGHT = 100;
    private final float TEXT_MARGIN_TOP;
    private final float TEXT_MARGIN_LEFT;

    private Rect mBodyRect;
    private Rect mResizableTopRect;
    private Rect mResizableBottomRect;
    private Paint mBodyPaint;
    private Paint mResizableTopRectPaint;
    private Paint mResizableBottomRectPaint;
    private TextPaint mBodyTextPaint;
    private float mTextMarginTop;
    private float mTextMarginLeft;

    private GestureDetectorCompat mDetector;
    private boolean showResizableRect = false;

    public MoveableView(Context context) {
        this(context, null);
    }

    public MoveableView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs,R.styleable.MoveableViewAttrs, 0, 0);

        TEXT_MARGIN_TOP = typedArray.getDimension(
                R.styleable.MoveableViewAttrs_mv_text_margin_top, 40);
        TEXT_MARGIN_LEFT = typedArray.getDimension(
                R.styleable.MoveableViewAttrs_mv_text_margin_left, 30);

        initialize();
    }

    private void initialize() {
        mBodyRect = new Rect(200, 200, 500, 300);
        mBodyPaint = new Paint();
        mBodyPaint.setColor(Color.BLACK);
        mBodyPaint.setStyle(Paint.Style.FILL);

        final int RECT_OFFSET = (int) (mBodyRect.top * 0.4);

        mResizableTopRect = new Rect(mBodyRect.left, mBodyRect.top, mBodyRect.right,
                mBodyRect.bottom - RECT_OFFSET);
        mResizableTopRectPaint = new Paint();
        mResizableTopRectPaint.setColor(Color.RED);
        mResizableTopRectPaint.setStyle(Paint.Style.FILL);

        mResizableBottomRect = new Rect(mBodyRect.left, mBodyRect.top + RECT_OFFSET,
                mBodyRect.right, mBodyRect.bottom);
        mResizableBottomRectPaint = new Paint();
        mResizableBottomRectPaint.setColor(Color.RED);
        mResizableBottomRectPaint.setStyle(Paint.Style.FILL);

        mTextMarginTop = mBodyRect.top + TEXT_MARGIN_TOP;
        mTextMarginLeft = mBodyRect.left + TEXT_MARGIN_LEFT;

        mBodyTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mBodyTextPaint.setColor(Color.WHITE);
        mBodyTextPaint.setTextSize(getResources().getDimension(R.dimen.default_text_size));
        mBodyTextPaint.setStyle(Paint.Style.FILL);

        mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(mBodyRect, mBodyPaint);
        canvas.drawText("Test", mTextMarginLeft, mTextMarginTop, mBodyTextPaint);

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
                if(touchedRect.rectType == RectType.TOP) {
                    mBodyRect.top -= distanceY;
                    mTextMarginTop = mBodyRect.top + TEXT_MARGIN_TOP;
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
            if(showResizableRect && isTouchInsideARect(event, mResizableTopRect)) {
                return new TouchedRect(mResizableTopRect, RectType.TOP);
            } else if(showResizableRect && isTouchInsideARect(event, mResizableBottomRect)) {
                return new TouchedRect(mResizableBottomRect, RectType.BOTTOM);
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
