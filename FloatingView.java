package com.kricherer.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.kricherer.raven.utils.ScreenUtils;

/**
 * Created by YP on 1/8/2016.
 */
public abstract class FloatingView extends android.support.v7.widget.AppCompatImageView {
    private final int CLICK_DISTANCE = 25;

    protected WindowManager mWindowManager = null;

    public FloatingView(Context context) {
        super(context);
        defaultInit();
        init();
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultInit();
        init();
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit();
        init();
    }

    private void defaultInit() {

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onCustomDraw(canvas);
    }

    public void setDragable(WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
        mWindowManager = windowManager;
        setOnTouchListener(new DraggableOnTouchListener(windowManager, layoutParams));
    }

    public void unsetDragable() {
        setOnTouchListener(null);
    }

    private class DraggableOnTouchListener implements OnTouchListener {
        private WindowManager mWindowManager = null;
        private WindowManager.LayoutParams mLayoutParams = null;
        private Point mScreenSize = null;
        private int mInitialX;
        private int mInitialY;
        private float mInitialTouchX;
        private float mInitialTouchY;
        private int mNewX, mNewY;

        public DraggableOnTouchListener(WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
            mWindowManager = windowManager;
            mLayoutParams = layoutParams;
            mScreenSize = ScreenUtils.getScreenSize(getResources());
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mInitialX = mLayoutParams.x;
                    mInitialY = mLayoutParams.y;
                    mInitialTouchX = event.getRawX();
                    mInitialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_UP:
                    int x = mNewX - mInitialX;
                    int y = mNewY - mInitialY;
                    double d = Math.sqrt(x * x + y * y);
                    if(d < CLICK_DISTANCE) {
                        view.callOnClick();
                    }
                    else if(d > 0) {
                        if(mNewX > (mScreenSize.x / 2))
                            mNewX = mScreenSize.x - getWidth() - getGap();
                        else if(mNewX < (mScreenSize.x / 2))
                            mNewX = getGap();

                        if(mNewY < getGap())
                            mNewY = getGap();
                        else if(mNewY > mScreenSize.y - getGap())
                            mNewY = mScreenSize.y - getGap();

                        moveTo(mNewX, mNewY, 0, 0);

                        onViewMoveCompleted(mNewX, mNewY);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    mNewX = mInitialX + (int) (event.getRawX() - mInitialTouchX);
                    mNewY = mInitialY + (int) (event.getRawY() - mInitialTouchY);
                    int dX = mNewY - mInitialX;
                    int dY = mNewY - mInitialY;
                    moveTo(mNewX, mNewY, dX, dY);

                    onViewMoving(mNewX, mNewY);
                    return true;
            }
            return false;
        }

        private void moveTo(int xPosition, int yPosition, int deltaX, int deltaY) {
            if(xPosition >= 0)
                mLayoutParams.x = xPosition;

            if(yPosition >= 0)
                mLayoutParams.y = yPosition;

            mWindowManager.updateViewLayout(FloatingView.this, mLayoutParams);
        }
    }

    protected abstract void init();

    protected abstract void onCustomDraw(Canvas canvas);

    public abstract int getGap();

    protected abstract void onViewMoving(int x, int y);

    protected abstract void onViewMoveCompleted(int x, int y);
}
