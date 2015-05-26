package com.rm.mywater.util;

import android.content.Context;
import android.os.Build;

/**
 * Created by alex
 */
public final class Dimen {

    private static Context sContext;

    public static void init(Context context){
        Dimen.sContext = context;
    }

    public static int getStatusBarHeight() {

        int result = 0;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return result;
        }

        int resourceId = sContext
                .getResources()
                .getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = sContext.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static int get(int resourceId) {
        return sContext.getResources().getDimensionPixelSize(resourceId);
    }

}
