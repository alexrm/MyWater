package com.rm.mywater.util.base;

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

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long savedToday = Prefs.getSavedToday();

        if (savedToday != TimeUtil.getStartOfTheDay(TimeUtil.unixTime())) {

            Log.d("SavedToday", "Today = " + savedToday);

            Prefs.clear();

            DrinkHistoryDatabase.addDay(
                    this,
                    new Day(
                            Prefs.getCurrentVol(),
                            Prefs.getBaseVol(),
                            TimeUtil.getStartOfTheDay(TimeUtil.unixTime())
                    )
            );

            Prefs.saveToday();

        } else {

            Log.d("SavedToday", "Today == Saved");

        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {

            setSupportActionBar(mToolbar);

            int paddingTop = Dimen.getStatusBarHeight();

//            mToolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
            mToolbar.setPadding(0, paddingTop, 0, 0);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
