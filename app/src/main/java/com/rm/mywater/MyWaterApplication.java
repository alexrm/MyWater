package com.rm.mywater;

import android.app.Application;

import com.rm.mywater.util.DrinkUtil;
import com.rm.mywater.util.Prefs;

/**
 * Created by alex on 08/04/15.
 */
public class MyWaterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Prefs.init(this);
        DrinkUtil.init(this);
    }
}
