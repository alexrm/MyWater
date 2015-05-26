package com.rm.mywater.views.wave;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by John on 2014/10/15.
 */
public class WaveView extends LinearLayout {

    private int mProgress;
    private int mWaveToTop;

    private Wave mWave;
    private Solid mSolid;
    private LayoutParams mSolidParams;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        mWave = new Wave(context);
        mWave.initializeWaveSize();
        mWave.initializePainters();

        mSolid = new Solid(context);
        mSolid.setBlowWavePaint(mWave.getBlowWavePaint());

        mSolidParams = (LayoutParams) mSolid.getLayoutParams();

        addView(mWave);
        addView(mSolid);

        setProgress(mProgress);
    }

    public void setProgress(int progress) {

        this.mProgress = progress > 100 ? 100 : (progress > 0) ? progress : 1;
        computeWaveToTop();
    }

    public Wave getWave() {
        return mWave;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus) {
            computeWaveToTop();
        }
    }

    private void computeWaveToTop() {

        if (mProgress == 100) {

            mWave.setVisibility(GONE);

            if (mSolidParams != null) {

                mSolidParams.height = LayoutParams.MATCH_PARENT;
                mSolidParams.weight = 0;
                mSolid.setLayoutParams(mSolidParams);
            }

        } else {

            if (mSolidParams != null) {

                mSolidParams.height = LayoutParams.WRAP_CONTENT;
                mSolidParams.weight = 1;
                mSolid.setLayoutParams(mSolidParams);
            }

            mWave.setVisibility(VISIBLE);
            mWaveToTop = (int) (getHeight() * (1f - mProgress / 100f) + 3);

            ViewGroup.LayoutParams waveParams = mWave.getLayoutParams();

            if (waveParams != null) {

                ((LayoutParams) waveParams).topMargin = mWaveToTop;
            }

            mWave.setLayoutParams(waveParams);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.progress = mProgress;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
    }

    private static class SavedState extends BaseSavedState {

        int progress;

        /**
         * Constructor called from {@link android.widget.ProgressBar#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
