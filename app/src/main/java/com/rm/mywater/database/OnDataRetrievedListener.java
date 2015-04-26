package com.rm.mywater.database;

import java.util.Collection;

/**
 * Created by alex on 24/04/15.
 */
public interface OnDataRetrievedListener {

    void onDataReceived(Collection<?> data);

    void onError(String err);

}
