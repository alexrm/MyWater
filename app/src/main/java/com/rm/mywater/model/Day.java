package com.rm.mywater.model;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by alex on 07/04/15.
 */
public class Day {

    private int  mCurVol;
    private int  mMaxVol;
    private long mStartTime;

    public Day(int curVol, int maxVol, long startTime) {

        this.mCurVol = curVol;
        this.mMaxVol = maxVol;
        this.mStartTime = startTime;
    }

    public Day() {

        // empty constructor
    }

    public static Day getDummy() {

        Day d = new Day();
        Random r = new Random();
        Calendar c = Calendar.getInstance();

        c.set(Calendar.MONTH, 4);
        c.set(Calendar.DAY_OF_MONTH, r.nextInt(30));

        d.setCurVol(r.nextInt(1800));
        d.setMaxVol(r.nextInt(1800));
        d.setStartTime(c.getTimeInMillis());

        return d;
    }

    //region Setters

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public void setCurVol(int curVol) {
        mCurVol = curVol;
    }

    public void setMaxVol(int maxVol) {
        mMaxVol = maxVol;
    }
    //endregion

    //region Getters
    public int getPercent() {

        int percent = (int) ((float) mCurVol / mMaxVol * 100);

        return percent > 100 ? 100 : percent;
    }

    public long getStartTime() {
        return mStartTime;
    }

    // helper method
    public long getEndTime() {

        return mStartTime + 3600*24;
    }

    public int getCurVol() {
        return mCurVol;
    }

    public int getMaxVol() {
        return mMaxVol;
    }
    //endregion
}
