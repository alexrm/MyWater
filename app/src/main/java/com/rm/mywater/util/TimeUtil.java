package com.rm.mywater.util;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by alex on 12/04/15.
 */
public class TimeUtil {

    private static final String TAG = "TimeUtil";

    private static Context sContext;

    public static void init(Context context) {

        sContext = context;
    }

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

    public static boolean isNotificationTime() {

        Calendar currentTime = Calendar.getInstance();

        int start = Prefs.get(sContext).getInt(Prefs.KEY_ALARM_START_FROM, 9);
        int end   = Prefs.get(sContext).getInt(Prefs.KEY_ALARM_WORK_UNTIL, 18);

        Log.d(TAG, "Start time: " + start + " End time: " + end);

        int hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY);

        Log.d(TAG, "Hour of day: " + hourOfDay);

        return hourOfDay >= start && hourOfDay < end;

    }

}
