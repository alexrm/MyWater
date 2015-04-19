package com.rm.mywater.schedule;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.rm.mywater.MainActivity;
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

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.stat_notify_chat)
                            .setContentTitle("Water")
                            .setContentText("Time to drink!");

            Intent resultIntent = new Intent(context, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(100, mBuilder.build());
        }
    }
}
