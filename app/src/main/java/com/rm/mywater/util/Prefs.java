package com.rm.mywater.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by alex on 08/04/15.
 */
public final class Prefs {

    private static final String TAG = "Prefs";

    //region Keys

    //region Current data keys
    // current volume, which user has drunk
    public static final String KEY_CURRENT_USER_VOL     = "cur_user";
    public static final String KEY_CURRENT_TARGET_VOL   = "cur_target";
    public static final String KEY_CURRENT_PERCENT      = "percent";
    //endregion

    //region Alarm keys
    public static final String KEY_ALARM_START_FROM     = "from";
    public static final String KEY_ALARM_WORK_UNTIL     = "until";

    public static final String KEY_ALARM_INTERVAL       = "interval";
    public static final String KEY_ALARM_STATE          = "alarm_state";
    //endregion

    //region User settings
    public static final String KEY_UNIT                 = "unit";
    public static final String KEY_USER_WEIGHT          = "user_weight";
    public static final String KEY_USER_GENDER          = "user_gender";

    // this one depends on gender and weight
    public static final String KEY_BASE_TARGET_VOL      = "base_target";
    //endregion

    //region Database keys
    public static final String KEY_DB_SAVED_TODAY = "day_is_today";
    //endregion


    //endregion

    private static final String PREF_MAIN_NAME = "WaterPreferences";

    private static SharedPreferences        sPreferences;
    private static SharedPreferences.Editor sEditor;

    public static void init(Context context) {

        sPreferences        = context.getSharedPreferences(PREF_MAIN_NAME, Context.MODE_PRIVATE);
        sEditor             = sPreferences.edit();
    }

    //region Pref methods
    public static <T> void putAndCommit(String key, T value) {

        put(key, value);
        commit();
    }

    public static <T> void put(String key, T value) {

        if (value instanceof Integer) {

            sEditor.putInt(key, (Integer) value);
        }

        else if (value instanceof Long) {

            sEditor.putLong(key, (Long) value);
        }

        else if (value instanceof String) {

            sEditor.putString(key, (String) value);
        }

        else if (value instanceof Float) {

            sEditor.putFloat(key, (Float) value);
        }

        else if (value instanceof Boolean) {

            sEditor.putBoolean(key, (Boolean) value);
        }

        Log.d(TAG, "put: key(" + key + ") value(" + value + ")");

    }

    public static SharedPreferences get() {

        return sPreferences;
    }

    public static SharedPreferences get(Context c) {

        init(c);
        return sPreferences;
    }

    public static void commit() {

        sEditor.commit();
    }
    //endregion

    //region Days methods
    public static long getSavedToday() {

        return sPreferences.getLong(KEY_DB_SAVED_TODAY, 0);
    }

    public static void saveToday() {

        putAndCommit(KEY_DB_SAVED_TODAY, TimeUtil.getStartOfTheDay(TimeUtil.unixTime()));
    }

    //endregion

    public static int getCurrentMax() {

        int curMax = sPreferences.getInt(KEY_CURRENT_TARGET_VOL, getBaseVol());

        Log.d("Prefs", "getCurrentMax - curMax: "
                + curMax);

        return curMax;
    }

    public static int getCurrentVol() {

        int curVol = sPreferences.getInt(KEY_CURRENT_USER_VOL, 0);

        Log.d("Prefs", "getCurrentVol - curVol: "
                + curVol);

        return curVol;
    }

    public static int getBaseVol() {

        int baseVol = sPreferences.getInt(KEY_BASE_TARGET_VOL, 1800);

        Log.d(TAG, "getBaseVol: " + baseVol);

        return baseVol;
    }

    // TODO make everything 0 every day
    public static void clear() {

        Log.d(TAG, "Clear");

        sEditor.putInt(KEY_CURRENT_PERCENT, 0);
        sEditor.putInt(KEY_CURRENT_USER_VOL, 0);
        sEditor.putInt(KEY_CURRENT_TARGET_VOL, getBaseVol());
        sEditor.commit();
    }

    public static String getUnit() {
        return get().getString(KEY_UNIT, "мл");
    }
}
