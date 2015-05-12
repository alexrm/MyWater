package com.rm.mywater.util;

import android.content.Context;
import android.graphics.Color;

import com.rm.mywater.model.Drink;

/**
 * Created by alex on 07/04/15.
 */
public final class DrinkUtil {

    private static Context sContext;

    public static void init(Context context) {

        sContext = context;
    }

    // TODO counts percent from drunk volume form prefs and user suggested daily volume
    // TODO can raise daily normal
    public static void countVolume(Drink drink) {

    }
//
//    public static float getKg(float lbs) {
//
//        return lbs/2.2F;
//    }
//
//    public static float getLbs(float kg) {
//
//        return kg*2.2F;
//    }
//
//    public static int getOz(float milliliters) {
//
//        return Math.round(milliliters * 0.034F);
//    }
//
//    public static float getMilliliters(float oz) {
//
//        return oz/0.034F;
//    }

    public static Number getStyledVolume(float vol) {

        // TODO implement this
        return 0;
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

            case Drink.OTHER: return "Другое";

            default: return "Другое";
        }
    }

    public static int getDrinkColor(int type) {

        switch (type) {

            case Drink.WATER: return Color.parseColor("#2c97d5");

            case Drink.ALCOHOL: return Color.parseColor("#e82a13");

            case Drink.TEA: return Color.parseColor("#ffe666");

            case Drink.COFFEE: return Color.parseColor("#cc8154");

            case Drink.ENERGY: return Color.parseColor("#08dfaa");

            case Drink.JUICE: return Color.parseColor("#ff9d13");

            case Drink.MILK: return Color.parseColor("#FFFBDE");

            case Drink.SODA: return Color.parseColor("#c995ff");

            case Drink.OTHER: return Color.parseColor("#d9d9d9");

            default: return Color.parseColor("#d9d9d9");
        }
    }
}
