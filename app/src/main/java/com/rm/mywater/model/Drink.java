package com.rm.mywater.model;

import android.util.Log;

import com.rm.mywater.util.TimeUtil;

import java.util.Random;

/**
 * Created by alex on 07/04/15.
 */
public class Drink {

    //region Drink ids
    public static final int WATER       = 1;
    public static final int TEA         = 2;
    public static final int COFFEE      = 3;
    public static final int MILK        = 4;
    public static final int SODA        = 5;
    public static final int ALCOHOL     = 6;
    public static final int JUICE       = 7;
    public static final int ENERGY      = 8;
    public static final int OTHER       = 9;
    //endregion

    //region Coefficients
    private static final float WATER_COEFFICIENT        = 1.0F;
    private static final float TEA_COEFFICIENT          = 0.85F;
    private static final float COFFEE_COEFFICIENT       = 0.8F;
    private static final float MILK_COEFFICIENT         = 0.78F;
    private static final float SODA_COEFFICIENT         = 0.22F;
    private static final float ALCOHOL_COEFFICIENT      = -5.0F;
    private static final float JUICE_COEFFICIENT        = 0.3F;
    private static final float ENERGY_COEFFICIENT       = 0.3F;
    private static final float OTHER_COEFFICIENT        = 0.1F;
    //endregion

    private float mCoefficient;

    private int         mType;
    private float       mVolume;
    private long        mTime; // unix time

    public Drink(int type, float volume) {

        setType(type);
        setVolume(volume);

        setTime(TimeUtil.unixTime());
    }

    public Drink() {

        // empty constructor
    }

    // for debug
    public static Drink getDummy() {

        Log.d("DRINK", "Getting dummy");

        Random r = new Random();

        return new Drink(r.nextInt(8) + 1, r.nextInt(1000));
    }

    //region Getters
    public int getType() {
        return mType;
    }

    public float getVolume() {
        return mVolume;
    }

    public float getCoefficient() {
        return mCoefficient;
    }

    public long getTime() {
        return mTime;
    }
    //endregion

    //region Setters
    public void setVolume(float volume) {
        mVolume = volume;
    }

    public void setType(int type) {
        mType = type;
        setCoefficient(type);
    }

    public void setTime(long time) {
        mTime = time;
    }

    private void setCoefficient(int type) {

        switch (type) {

            case WATER: {

                mCoefficient = WATER_COEFFICIENT;
                break;
            }

            case TEA: {

                mCoefficient = TEA_COEFFICIENT;
                break;
            }

            case COFFEE: {

                mCoefficient = COFFEE_COEFFICIENT;
                break;
            }

            case MILK: {

                mCoefficient = MILK_COEFFICIENT;
                break;
            }

            case SODA: {

                mCoefficient = SODA_COEFFICIENT;
                break;
            }

            case ALCOHOL: {

                mCoefficient = ALCOHOL_COEFFICIENT;
                break;
            }

            case JUICE: {

                mCoefficient = JUICE_COEFFICIENT;
                break;
            }

            case ENERGY: {

                mCoefficient = ENERGY_COEFFICIENT;
                break;
            }

            case OTHER: {

                mCoefficient = OTHER_COEFFICIENT;
                break;
            }
        }
    }
    //endregion
}


