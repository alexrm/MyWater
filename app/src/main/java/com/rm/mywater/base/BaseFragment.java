package com.rm.mywater.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

/**
 * Created by alex on 19/04/15.
 */
public class BaseFragment extends Fragment {

    protected Toolbar mToolbar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mToolbar = ((BaseActivity) activity).getToolbar();
    }
}
