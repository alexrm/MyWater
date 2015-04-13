package com.rm.mywater.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rm.mywater.model.Day;
import com.rm.mywater.model.Drink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 07/04/15.
 */
public class DrinkHistoryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "water_data";

    //region Days table
    private static final String DAYS_TABLE      = "days";

    private static final String COL_START_TIME  = "start_time";
    private static final String COL_PERCENT     = "percent";
    //endregion

    //region Timeline table
    private static final String TIMELINE_TABLE = "timeline";
    private static final String COL_TIME = "time";
    private static final String COL_DRINK_ID = "dr_id";
    private static final String COL_VOLUME = "volume";
    //endregion

    //region Create table commands
    private static final String DAYS_TABLE_CREATE =
            "create table " +
                    DAYS_TABLE +
                    " ( " +
                    COL_START_TIME   + " INTEGER PRIMARY KEY," +
                    COL_PERCENT      + " INTEGER NOT NULL," +
                    " ) ";

    private static final String TIMELINE_TABLE_CREATE =
            "create table " +
                    TIMELINE_TABLE +
                    " ( " +
                    COL_TIME        + " INTEGER PRIMARY KEY," +
                    COL_DRINK_ID    + " INTEGER NOT NULL," +
                    COL_VOLUME      + " FLOAT NOT NULL," +
                    " ) ";
    //endregion

    public DrinkHistoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DAYS_TABLE_CREATE);
        db.execSQL(TIMELINE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    //region Main getters
    public static List<Drink> getTimeline(Context context, Day day) {

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

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                Drink drink = new Drink();

                drink.setType(cursor.getInt(cursor.getColumnIndex(COL_DRINK_ID)));
                drink.setTime(cursor.getLong(cursor.getColumnIndex(COL_TIME)));
                drink.setVolume(cursor.getFloat(cursor.getColumnIndex(COL_VOLUME)));

                drinks.add(drink);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        Collections.reverse(drinks);

        return drinks;
    }

    // should return even empty days
    public static List<Day> getDays(Context context) {

        SQLiteDatabase  db      = new DrinkHistoryDatabase(context).getReadableDatabase();
        ArrayList<Day>  rawDays = new ArrayList<>();
        ArrayList<Day>  days;

        String selectQuery =
                "SELECT * FROM " + DAYS_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                Day day = new Day();

                day.setPercent(cursor.getInt(cursor.getColumnIndex(COL_PERCENT)));
                day.setStartTime(cursor.getInt(cursor.getColumnIndex(COL_START_TIME)));

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        days = formatList(rawDays);

        Collections.reverse(days);

        return days;
    }
    //endregion

    //region Add methods
    public static void addDrink(Context context, Drink drink) {

        SQLiteDatabase db = new DrinkHistoryDatabase(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TIME, drink.getTime());
        values.put(COL_DRINK_ID, drink.getType());
        values.put(COL_VOLUME, drink.getVolume());

        db.insert(TIMELINE_TABLE, null, values);
        db.close();

    }

    public static void addDay(Context context, Day day) {

        SQLiteDatabase db = new DrinkHistoryDatabase(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_START_TIME, day.getStartTime());
        values.put(COL_PERCENT, day.getPercent());

        db.insert(DAYS_TABLE, null, values);
        db.close();

    }
    //endregion

    //region Update
    public static void updateDay(Context context, Day day) {

        SQLiteDatabase db = new DrinkHistoryDatabase(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PERCENT, day.getPercent());

        // updating row
        db.update(
                DAYS_TABLE,
                values,
                COL_START_TIME + " = " + day.getStartTime(),
                null
        );

        db.close();
    }
    //endregion

    //region Delete
    public static void deleteDrink(Context context, Drink drink) {

        SQLiteDatabase db = new DrinkHistoryDatabase(context).getWritableDatabase();

        db.delete(TIMELINE_TABLE, COL_TIME + " = " + drink.getTime(), null);

        db.close();
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

    //region Helper method
    private static ArrayList<Day> formatList(ArrayList<Day> rawDays) {

        ArrayList<Day> res = new ArrayList<>();
        int size = rawDays.size();

        if (size > 1) {

            for (int i = 0; i < size-1; i++) {

                Day current     = rawDays.get(i);
                Day next        = rawDays.get(i+1);

                res.add(current);

                long startTime = current.getEndTime();

                while (startTime < next.getStartTime()) {

                    res.add(new Day(0, startTime));

                    startTime += 3600 * 24;
                }

            }

            res.add(rawDays.get(size - 1));

        } else {

            return rawDays;
        }

        return res;
    }
    //endregion
}
