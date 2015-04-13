package com.rm.mywater.util;

import android.content.Context;

import com.rm.mywater.model.Drink;

/**
 * Created by alex on 07/04/15.
 */
public class DrinkUtil {

    private static Context sContext;

    public static void init(Context context) {

        sContext = context;
    }

    // TODO counts percent from drunk volume form prefs and user suggested daily volume
    // TODO can raise daily normal
    public static void countVolume(Drink drink) {


    }

    public static float getKg(float lbs) {

        return lbs/2.2F;
    }

    public static float getLbs(float kg) {

        return kg*2.2F;
    }

    public static float getOz(float liters) {

        return liters*34;
    }

    public static float getLiters(float oz) {

        return oz/34;
    }
}
