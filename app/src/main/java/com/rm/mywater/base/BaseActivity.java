package com.rm.mywater.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.rm.mywater.R;
import com.rm.mywater.database.DrinkHistoryDatabase;
import com.rm.mywater.model.Day;
import com.rm.mywater.util.Dimen;
import com.rm.mywater.util.Prefs;
import com.rm.mywater.util.TimeUtil;

/**
 * Created by alex on 14/04/15.
 */
public class BaseActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long savedToday = Prefs.getSavedToday();

        if (savedToday != TimeUtil.getToday()) {

            Log.d("SavedToday", "Today = " + savedToday);

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

            Log.d("SavedToday", "Today == Saved");

            Prefs.put(Prefs.KEY_DB_DAY_EXISTS, true);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {

            setSupportActionBar(toolbar);

            int paddingTop = Dimen.getStatusBarHeight();

//            toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
            toolbar.setPadding(0, paddingTop, 0, 0);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
