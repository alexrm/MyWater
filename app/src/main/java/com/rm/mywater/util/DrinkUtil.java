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

    public static float getOz(float milliliters) {

        return milliliters*0.034F;
    }

    public static float getMilliliters(float oz) {

        return oz/0.034F;
    }

    public static String getTitle(int type) {

        switch (type) {

            case Drink.WATER: return "Вода";

            case Drink.ALCOHOL: return "Алкоголь";

            case Drink.TEA: return "Чай";

            case Drink.COFFEE: return "Кофе";

            case Drink.ENERGY: return "Энергетик";

            case Drink.JUICE: return "Сок";

            case Drink.MILK: return "Молоко";

            case Drink.SODA: return "Газировка";

            case Drink.OTHER: return "Другой напиток";

            default: return "Другой напиток";
        }
    }

    public static int getDrinkIcon(int type) {

        // TODO implement this

        return -1;
    }
}
