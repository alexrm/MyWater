package com.rm.mywater;

import android.app.Application;

import com.rm.mywater.database.DrinkHistoryDatabase;
import com.rm.mywater.model.Day;
import com.rm.mywater.util.DrinkUtil;
import com.rm.mywater.util.Prefs;
import com.rm.mywater.util.TimeUtil;

/**
 * Created by alex on 08/04/15.
 */
public class MyWaterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Prefs.init(this);
        DrinkUtil.init(this);

        long savedToday = Prefs.getSavedToday();

        if (savedToday != TimeUtil.getToday()) {

            Prefs.clear();

            DrinkHistoryDatabase.addDay(
                    this,
                    new Day(
                            Prefs.getPercent(),
                            TimeUtil.getToday()
                    )
            );

            Prefs.saveToday();

            Prefs.put(Prefs.KEY_DB_DAY_EXISTS, true);

        } else {

            Prefs.put(Prefs.KEY_DB_DAY_EXISTS, true);
        }
    }
}
