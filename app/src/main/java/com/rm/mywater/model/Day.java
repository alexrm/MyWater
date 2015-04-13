package com.rm.mywater.model;

/**
 * Created by alex on 07/04/15.
 */
public class Day {

    private int  mPercent;
    private long mStartTime;

    public Day(int percent, long startTime) {

        this.mPercent = percent;
        this.mStartTime = startTime;
    }

    public Day() {

        // empty constructor
    }

    //region Setters
    public void setPercent(int percent) {
        mPercent = percent;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }
    //endregion

    //region Getters
    public int getPercent() {
        return mPercent;
    }

    public long getStartTime() {
        return mStartTime;
    }

    // helper method
    public long getEndTime() {

        return mStartTime + 3600*24;
    }
    //endregion
}
