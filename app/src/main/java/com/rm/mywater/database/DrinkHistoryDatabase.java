package com.rm.mywater.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rm.mywater.model.Day;
import com.rm.mywater.model.Drink;
import com.rm.mywater.util.Prefs;
import com.rm.mywater.util.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alex on 07/04/15.
 */
public class DrinkHistoryDatabase extends SQLiteOpenHelper {

    private static final String TAG = "DrinkHistoryDatabase";

    private static final String DATABASE_NAME = "water_data";

    private static OnDataUpdatedListener sUpdateListener;

    //region Days table
    private static final String DAYS_TABLE          = "days";

    private static final String COL_START_TIME      = "start_time";
    private static final String COL_MAX_VOLUME      = "max_vol";
    private static final String COL_CUR_VOLUME      = "cur_vol";
    //endregion

    //region Timeline table
    private static final String TIMELINE_TABLE = "timeline";
    private static final String COL_TIME = "time";
    private static final String COL_DRINK_ID = "dr_id";
    private static final String COL_VOLUME = "volume";
    //endregion

    // region Support columns
    public static final String COL_OVERALL = "overall_volume";
    public static final String COL_SYNCABLE_VOLUME = "sync_vol";
    // endregion

    //region Create table commands
    private static final String DAYS_TABLE_CREATE =
            "create table " +
                    DAYS_TABLE +
                    " ( " +
                    COL_START_TIME   + " INTEGER PRIMARY KEY," +
                    COL_CUR_VOLUME   + " INTEGER NOT NULL, " +
                    COL_MAX_VOLUME   + " INTEGER NOT NULL" +
                    " ) ";

    private static final String TIMELINE_TABLE_CREATE =
            "create table " +
                    TIMELINE_TABLE +
                    " ( " +
                    COL_TIME        + " INTEGER PRIMARY KEY," +
                    COL_DRINK_ID    + " INTEGER NOT NULL," +
                    COL_VOLUME      + " INTEGER NOT NULL" +
                    " ) ";
    //endregion

    public DrinkHistoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static void setUpdateListener(OnDataUpdatedListener updateListener) {
        sUpdateListener = updateListener;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate DB");

        db.execSQL(DAYS_TABLE_CREATE);
        db.execSQL(TIMELINE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    //region Main getters
    public static synchronized void retrieveTimeline(Context context,
                                                     Day day,
                                                     OnDataRetrievedListener listener) {

        if (day == null) {

            listener.onError("Null day");
            return;
        }

        SQLiteDatabase      db       = new DrinkHistoryDatabase(context).getReadableDatabase();
        ArrayList<Drink>    drinks   = new ArrayList<>();

        String selectQuery =
                "SELECT *"
                + " FROM "
                        + TIMELINE_TABLE
                + " WHERE "
                        + COL_TIME + " >= " + day.getStartTime()
                + " AND "
                        + COL_TIME + " < " + day.getEndTime();

        Log.d(TAG, "Query for retrieveTimeline is:\n" + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                Drink drink = new Drink();

                drink.setType(cursor.getInt(cursor.getColumnIndex(COL_DRINK_ID)));
                drink.setTime(cursor.getLong(cursor.getColumnIndex(COL_TIME)));
                drink.setVolume(cursor.getInt(cursor.getColumnIndex(COL_VOLUME)));

                drinks.add(drink);

            } while (cursor.moveToNext());

        } else {

            cursor.close();
            db.close();

            listener.onError("Database error, cannot load timeline");

            return;
        }

        cursor.close();
        db.close();

        Collections.reverse(drinks);

        Log.d(TAG, "Get timeline for " + day.getStartTime() + ": " + drinks.size() + " drinks");

        listener.onDataReceived(drinks);
    }

    // should return empty days
    public static synchronized void retrieveDays(Context context,
                                                 OnDataRetrievedListener listener) {

        SQLiteDatabase  db      = new DrinkHistoryDatabase(context).getReadableDatabase();
        ArrayList<Day>  rawDays = new ArrayList<>();
        ArrayList<Day>  days;

        String selectQuery =
                "SELECT * FROM " + DAYS_TABLE;

        Log.d(TAG, "Query for retrieveDays is:\n" + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                long startTime = cursor.getInt(cursor.getColumnIndex(COL_START_TIME));
                int curVol = cursor.getInt(cursor.getColumnIndex(COL_CUR_VOLUME));
                int maxVol = cursor.getInt(cursor.getColumnIndex(COL_MAX_VOLUME));

                Day day = new Day();

                day.setStartTime(startTime);
                day.setCurVol(curVol);
                day.setMaxVol(maxVol);

                rawDays.add(day);

            } while (cursor.moveToNext());

        } else {

            cursor.close();
            db.close();

            listener.onError("Retrieve days cursor failure, " +
                    "cursor rows count: " + cursor.getCount());

            return;
        }

        cursor.close();
        db.close();

        days = formatList(rawDays);

        Collections.reverse(days);

        Log.d(TAG, "Get days, count: " + days.size());

        listener.onDataReceived(days);
    }

    public static synchronized void retrieveOverall(Context context,
                                                    OnDataRetrievedListener listener) {

        SQLiteDatabase database = new DrinkHistoryDatabase(context).getReadableDatabase();
        String query = generateOverallQuery();

        ArrayList<Drink> result = new ArrayList<>();

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            int type = -1;

            do {

                type++;

                Drink drink = new Drink();

                drink.setType(type);
                drink.setVolume(cursor.getInt(cursor.getColumnIndex(COL_OVERALL)));

                result.add(drink);

            } while (cursor.moveToNext());

        } else {

            cursor.close();
            database.close();

            listener.onError("Database error, cannot load timeline");

            return;
        }

        cursor.close();
        database.close();

        listener.onDataReceived(result);
    }
    //endregion

    //region Add methods
    public static void addDrink(Context context, Drink drink) {

        SQLiteDatabase db = new DrinkHistoryDatabase(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_TIME, drink.getTime());
        values.put(COL_DRINK_ID, drink.getType());
        values.put(COL_VOLUME, drink.getVolume());

        Log.d(TAG, "Adding drink, id = " + drink.getType() + " vol = " + drink.getVolume());

        db.insert(TIMELINE_TABLE, null, values);

        updateDay(db, drink, true);

    }

    public static void addDay(Context context, Day day) {

        SQLiteDatabase db = new DrinkHistoryDatabase(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_START_TIME, day.getStartTime());
        values.put(COL_MAX_VOLUME, Prefs.getBaseVol());
        values.put(COL_CUR_VOLUME, 0);

        Log.d(TAG, "addDay: startTime is " + day.getStartTime()
                + " current time is " + TimeUtil.unixTime());

        Log.d(TAG, "addDay: percent = " + day.getPercent());

        db.insert(DAYS_TABLE, null, values);
        db.close();

    }
    //endregion

    //region Update
    private static void updateDay(SQLiteDatabase database, Drink drink, boolean adding) {

        int     styledVolume    = (int) (drink.getVolume() * drink.getCoefficient());
        boolean isAlcohol       = styledVolume < 0;
        long    startOfTheDay   = TimeUtil.getStartOfTheDay(drink.getTime());

        if (startOfTheDay == Prefs.getSavedToday()) {

            int currentValue = isAlcohol ?
                    Prefs.getCurrentMax()
                    :
                    Prefs.getCurrentVol();

            Prefs.put(
                    isAlcohol ?
                            Prefs.KEY_CURRENT_TARGET_VOL
                            :
                            Prefs.KEY_CURRENT_USER_VOL,
                    adding ?
                            currentValue + Math.abs(styledVolume)
                            :
                            currentValue - Math.abs(styledVolume)
            );
            Prefs.commit();
        }

        String resultQuery =
                "UPDATE "
                        + DAYS_TABLE
                        + " SET "
                        + (isAlcohol ? COL_MAX_VOLUME : COL_CUR_VOLUME)
                        + " = "
                        + (isAlcohol ? COL_MAX_VOLUME : COL_CUR_VOLUME)
                        + (adding ? " + " : " - ")
                        + Math.abs(styledVolume)
                        + " WHERE "
                        + COL_START_TIME
                        + " = "
                        + startOfTheDay;

        Log.d("DrinkHistoryDatabase", "updateDay - resultQuery: "
                + resultQuery);

        // updating row
        database.execSQL(resultQuery);

        if (sUpdateListener != null) sUpdateListener.onUpdate();

        database.close();
    }

    public static void updateCurrentDay(Context c) {

        SQLiteDatabase database = new DrinkHistoryDatabase(c).getWritableDatabase();

        // updating row
        String resultQuery =
                "UPDATE "
                        + DAYS_TABLE
                        + " SET "
                        + COL_MAX_VOLUME
                        + " = "
                        + Prefs.getCurrentMax()
                        + " WHERE "
                        + COL_START_TIME
                        + " = "
                        + Prefs.getSavedToday();

        Log.d("DrinkHistoryDatabase", "updateCurrentDay - resultQuery: "
                + resultQuery);

        database.execSQL(resultQuery);
        database.close();
    }
    //endregion

    //region Delete
    public static void deleteDrink(Context context, Drink drink) {

        Log.d(TAG, "DeleteDrink: " + drink.getTime());

        SQLiteDatabase db = new DrinkHistoryDatabase(context).getWritableDatabase();

        db.delete(TIMELINE_TABLE, COL_TIME + " = " + drink.getTime(), null);

        updateDay(db, drink, false);
    }
    //endregion

    @Deprecated
    public static boolean isDayExist(Context context, long time) {

        SQLiteDatabase db = new DrinkHistoryDatabase(context).getReadableDatabase();

        Cursor cursor = db.query(
                DAYS_TABLE,
                null,
                COL_START_TIME + " = " + time,
                null,
                null,
                null,
                null
        );

        int rows = cursor.getCount();

        cursor.close();
        db.close();

        return !(rows == 0);
    }

    public static void clearTables(Context c) {

        SQLiteDatabase db = new DrinkHistoryDatabase(c).getWritableDatabase();

        db.delete(DAYS_TABLE, null, null);
        db.delete(TIMELINE_TABLE, null, null);

        db.close();
    }

    public static void drop(Context c) {

        SQLiteDatabase db = new DrinkHistoryDatabase(c).getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + DAYS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TIMELINE_TABLE);

        db.close();
    }

    //region Helper method
    private static ArrayList<Day> formatList(@NonNull ArrayList<Day> rawDays) {

        ArrayList<Day> res = new ArrayList<>();
        int size = rawDays.size();

        Log.d(TAG, "FormatList: size of initial list is " + size);

        if (size > 1) {

            for (int i = 0; i < size-1; i++) {

                Day current     = rawDays.get(i);
                Day next        = rawDays.get(i+1);

                res.add(current);

                long startTime = current.getEndTime();

                while (startTime < next.getStartTime()) {

                    res.add(new Day(0, 0, startTime));

                    startTime += 3600 * 24;
                }

            }

            // here we add the last one, because we avoid it in cycle
            res.add(rawDays.get(size - 1));

        } else {

            return rawDays;
        }

        return res;
    }

    private static String generateOverallQuery() {

        StringBuilder queryBuilder = new StringBuilder();

        String queryRoot = "SELECT"
                + " SUM" + "(" + COL_VOLUME + ")"
                + " AS " + COL_OVERALL
                + " FROM " + TIMELINE_TABLE;

        queryBuilder.append(queryRoot).append("\n\nUNION ALL\n\n");

        for (int i = 0 ; i < Drink.DRINK_TYPES.length ; i++) {

            queryBuilder.append(queryRoot)
                    .append(" WHERE ")
                    .append(COL_DRINK_ID).append(" = ").append(Drink.DRINK_TYPES[i])
                    .append(i == (Drink.DRINK_TYPES.length - 1) ? "" : "\n\nUNION ALL\n\n");
        }

        return queryBuilder.toString();
    }

    //endregion
}
