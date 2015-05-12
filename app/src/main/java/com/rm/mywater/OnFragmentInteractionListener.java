package com.rm.mywater;

/**
 * Created by alex
 */
public interface OnFragmentInteractionListener {

    int KEY_OPEN_DAY                = 10;

    int KEY_OPEN_STATS              = 0;
    int KEY_OPEN_NOTIFY             = 1;
    int KEY_OPEN_SETTINGS           = 2;

    <T> void onFragmentAction(T data, int key);
}
