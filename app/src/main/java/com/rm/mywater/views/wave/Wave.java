package com.rm.mywater.views.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.rm.mywater.R;
import com.rm.mywater.util.Dimen;

// y=Asin(ωx+φ)+k
public class Wave extends View {

    private final int   WAVE_COLOR              = Color.parseColor("#34aedd");
    private final int   WAVE_HEIGHT             = Dimen.get(R.dimen.wave_height);
    private final float WAVE_LENGTH_MULTIPLE    = 1.2f;
    private final float WAVE_SPEED              = 0.05f;
    private final int   WAVE_ALPHA              = 255;

    private final float X_SPACE     = 20;
    private final double PI2        = 2 * Math.PI;

    private Path mBlowWavePath      = new Path();
    private Paint mBlowWavePaint    = new Paint();

    private int mWaveHeight;
    private float mWaveMultiple;
    private float mWaveLength;
    private float mMaxRight;
    private float mWaveHz;

    // wave animation
    private float mBlowOffset;

    private RefreshProgressRunnable mRefreshProgressRunnable;

    private int left, right, bottom;
    // ω
    private double omega;
    private boolean mStopped = false;

    public Wave(Context context) {
        super(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mBlowWavePath, mBlowWavePaint);
//        canvas.drawPath(mAboveWavePath, mAboveWavePaint);
    }

    public Paint getBlowWavePaint() {
        return mBlowWavePaint;
    }

    public void initializeWaveSize() {

        mWaveMultiple   = WAVE_LENGTH_MULTIPLE;
        mWaveHeight     = WAVE_HEIGHT;
        mWaveHz         = WAVE_SPEED;

        mBlowOffset = mWaveHeight * 0.4f;
        
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                mWaveHeight * 2
        );

        setLayoutParams(params);
    }

    // TODO make public
    public void stopWave() {

        Log.d("Wave", "stopWave");

        mStopped = true;
        removeCallbacks(mRefreshProgressRunnable);
    }

    // TODO make public
    public void restartWave() {

        Log.d("Wave", "restartWave");

        postDelayed(new Runnable() {
            @Override
            public void run() {

                mStopped = false;
                removeCallbacks(mRefreshProgressRunnable);
                mRefreshProgressRunnable = new RefreshProgressRunnable();
                post(mRefreshProgressRunnable);
            }
        }, 300);

    }

    public void initializePainters() {

        mBlowWavePaint.setColor(WAVE_COLOR);
        mBlowWavePaint.setAlpha(WAVE_ALPHA);
        mBlowWavePaint.setStyle(Paint.Style.FILL);
        mBlowWavePaint.setAntiAlias(true);
    }

    private void calculatePath() {

        if (isStopped()) return;

        mBlowWavePath.reset();

        getWaveOffset();

        float y;

        mBlowWavePath.moveTo(left, bottom);

        for (float x = 0; x <= mMaxRight; x += X_SPACE) {

            y = (float) (
                    mWaveHeight * Math.sin(omega * x + mBlowOffset) + mWaveHeight
            );

            mBlowWavePath.lineTo(x, y);
        }

        mBlowWavePath.lineTo(right, bottom);
    }

    private boolean isStopped() {
        return mStopped;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        Log.d("Wave", "onWindowFocusChanged focus: " + hasWindowFocus);

        if (hasWindowFocus) {

            if (mWaveLength == 0) {

                startWave();
                restartWave();

            }
        }
    }

    private void startWave() {

        if (getWidth() != 0) {

            int width = getWidth();
            mWaveLength = width * mWaveMultiple;
            left = getLeft();
            right = getRight();
            bottom = getBottom();
            mMaxRight = right + X_SPACE;
            omega = PI2 / mWaveLength;
        }
    }

    private void getWaveOffset() {

        if (mBlowOffset > Float.MAX_VALUE - 100) {

            mBlowOffset = 0;

        } else {

            mBlowOffset += mWaveHz;
        }
    }

    private class RefreshProgressRunnable implements Runnable {

        public void run() {
            synchronized (Wave.this) {

                long start = System.currentTimeMillis();

                calculatePath();
                invalidate();

                long gap = 16 - (System.currentTimeMillis() - start);

                postDelayed(this, gap < 0 ? 0 : gap);
            }
        }
    }

}
