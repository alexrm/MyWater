package com.rm.mywater.model;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by alex on 07/04/15.
 */
public class Day {

    private int  mPercent; // 0% - 100%
    private long mStartTime;

    public Day(int percent, long startTime) {

        this.mPercent = percent;
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

        d.setPercent(r.nextInt(100));
        d.setStartTime(c.getTimeInMillis());

        return d;
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
