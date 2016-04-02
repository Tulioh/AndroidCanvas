package com.tulio.projecttotestthings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tulio on 3/31/16.
 */
public class MoveableView extends View implements View.OnTouchListener {
    private Rect mRect;
    private Paint mPaint;

    public MoveableView(Context context) {
        this(context, null);
    }

    public MoveableView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    private void initialize() {
        mRect = new Rect(200, 200, 300, 300);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(mRect, mPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                if (x >= mRect.left && x < (mRect.left + mRect.width())
                        && y >= mRect.top && y < (mRect.left + mRect.height())) {
                    Log.d("teste", "clicked inside of rectangle");
                }
                break;
        }

        return false;
    }
}
