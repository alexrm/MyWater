package com.rm.mywater.util;

import java.util.Calendar;

/**
 * Created by alex on 12/04/15.
 */
public class TimeUtil {

    public static long unixtime() {

        return System.currentTimeMillis()/1000;
    }

    public static long getToday() {

        // FIXME 2 calendars look like a shit

        Calendar actual = Calendar.getInstance();
        Calendar result = Calendar.getInstance();

        int year    = actual.get(Calendar.YEAR);
        int month   = actual.get(Calendar.MONTH);
        int day     = actual.get(Calendar.DAY_OF_MONTH);

        result.set(year, month, day, 0, 0, 0);

        return result.getTimeInMillis()/1000;
    }


}
