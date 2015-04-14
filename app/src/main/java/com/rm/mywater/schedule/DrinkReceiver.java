package com.rm.mywater.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rm.mywater.util.Prefs;
import com.rm.mywater.util.TimeUtil;

/**
 * Created by alex on 12/04/15.
 */
public class DrinkReceiver extends BroadcastReceiver {

    private static final String TAG = "DrinkReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        TimeUtil.init(context);

        if (!Prefs.get().getBoolean(Prefs.KEY_ALARM_STATE, false)) {

            Log.d(TAG, "Notifications stopped");
            return;
        }

        if (TimeUtil.isNotificationTime()) {

            // TODO implement notification sending
        }
    }
}
