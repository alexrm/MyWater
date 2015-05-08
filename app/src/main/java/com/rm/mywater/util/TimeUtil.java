package com.rm.mywater.util;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by alex on 12/04/15.
 */
public final class TimeUtil {

    private static final String TAG = "TimeUtil";
    private static Context sContext;

    public static void init(Context context) {

        sContext = context;
    }

    public static long unixTime() {

        return System.currentTimeMillis()/1000;
    }

    public static long getToday() {

        Calendar calendar = Calendar.getInstance();

        int year    = calendar.get(Calendar.YEAR);
        int month   = calendar.get(Calendar.MONTH);
        int day     = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, day, 0, 0, 0);

        long result = calendar.getTimeInMillis()/1000;

        Log.d(TAG, "Today in millis: " + result);

        return result;
    }

    public static String getTime(long unix) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unix * 1000);

        return String.format("%tR", calendar);
    }

    public static String getDay(long unix) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unix * 1000);

        return String.format("%te %tB", calendar, calendar);
    }

    public static boolean isNotificationTime() {

        Calendar currentTime = Calendar.getInstance();

        int hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY);

        Log.d(TAG, "Hour of day: " + hourOfDay);

        // FIXME if start > end
        int start = Prefs.get(sContext).getInt(Prefs.KEY_ALARM_START_FROM, 9);
        int end   = Prefs.get(sContext).getInt(Prefs.KEY_ALARM_WORK_UNTIL, 18);

        Log.d(TAG, "Start hour: " + start + " End hour: " + end);

        return (hourOfDay == start && hourOfDay == end) || (hourOfDay >= start && hourOfDay < end);

    }

}
