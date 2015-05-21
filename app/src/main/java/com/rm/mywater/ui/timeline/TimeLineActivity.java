package com.rm.mywater.ui.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rm.mywater.R;
import com.rm.mywater.model.Day;
import com.rm.mywater.util.base.BaseActivity;

public class TimeLineActivity extends BaseActivity {

    public static final String START_TIME_EXTRA = "startTime";
    public static final String TITLE_EXTRA = "title";

    public static final int REQUEST_UPDATE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (mToolbar != null) {

            getSupportActionBar().setTitle(getIntent().getStringExtra(TITLE_EXTRA));
            mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    onBackPressed();
                }
            });
        }

        showFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_OK, getIntent());
    }

    private void showFragment() {

        Intent parentIntent = getIntent();

        long startTime = parentIntent.getLongExtra(START_TIME_EXTRA, 0);
        Day srcDay = new Day();

        srcDay.setStartTime(startTime);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.container,
                        TimelineFragment.newInstance(srcDay)
                )
                .commit();
    }

}
