package com.rm.mywater;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rm.mywater.ui.NotificationsFragment;
import com.rm.mywater.ui.SettingsFragment;
import com.rm.mywater.ui.stats.StatisticsFragment;
import com.rm.mywater.ui.timeline.TimeLineActivity;
import com.rm.mywater.util.TimeUtil;
import com.rm.mywater.util.base.BaseActivity;
import com.rm.mywater.util.base.BaseFragment;


public class ContentActivity extends BaseActivity implements OnFragmentInteractionListener {

    public static final String KEY_FRAGMENT_EXTRA = "keyFragment";
    public static final String KEY_TITLE_EXTRA = "keyTitle";

    private BaseFragment mFragmentToShow;
    private int mKey;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        revealData();

        if (mToolbar != null) {

            mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            getSupportActionBar().setTitle(mTitle);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    onBackPressed();
                }
            });
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragmentToShow)
                .commit();
    }

    private void revealData() {

        mKey = getIntent().getIntExtra(KEY_FRAGMENT_EXTRA, 0);

        Log.d("ContentActivity", "revealData - key: "
                + mKey);

        switch (mKey) {

            case KEY_OPEN_STATS:
                mTitle = "Статистика";
                mFragmentToShow = new StatisticsFragment();
                break;

            case KEY_OPEN_NOTIFY:
                mTitle = "Уведомления";
                mFragmentToShow = new NotificationsFragment();
                break;

            case KEY_OPEN_SETTINGS:
                mTitle = "Настройки";
                mFragmentToShow = new SettingsFragment();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new StatisticsFragment())
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("ContentActivity", "onNewIntent");
    }

    @Override
    public <T> void onFragmentAction(T data, int key) {

        long startTime = (Long) data;

        if (key == KEY_OPEN_DAY) {

            Intent timelineIntent = new Intent(this, TimeLineActivity.class);
            timelineIntent.putExtra(TimeLineActivity.START_TIME_EXTRA, startTime);
            timelineIntent.putExtra(TimeLineActivity.TITLE_EXTRA, TimeUtil.getDay(startTime));

            startActivityForResult(timelineIntent, TimeLineActivity.REQUEST_UPDATE);
        }
    }
}
