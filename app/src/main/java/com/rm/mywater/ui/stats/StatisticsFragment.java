package com.rm.mywater.ui.stats;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rm.mywater.R;
import com.rm.mywater.util.base.BaseFragment;
import com.rm.mywater.views.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends BaseFragment {

    private static final int DAYS = 0;
    private static final int OVERALL = 1;

    private ViewPager        mViewPager;
    private SlidingTabLayout mFriendsTabs;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager      = (ViewPager)        view.findViewById(R.id.timeline_pager);
        mFriendsTabs    = (SlidingTabLayout) view.findViewById(R.id.timeline_tabs);

        mViewPager  .setAdapter(new TabPagerAdapter(getChildFragmentManager()));

        mFriendsTabs.setViewPager(mViewPager);
        mFriendsTabs.setDividerColors(Color.TRANSPARENT);
        mFriendsTabs.setSelectedIndicatorColors(Color.WHITE);
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter
            implements ViewPager.OnPageChangeListener {

        private OverallFragment mOverallFragment;
        private DaysFragment    mDaysFragment;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);

            mOverallFragment = new OverallFragment();
            mDaysFragment    = new DaysFragment();

            mFriendsTabs.setOnPageChangeListener(this);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case DAYS:    return mDaysFragment;
                case OVERALL: return mOverallFragment;
                default:      return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                case DAYS: {

                    return "ПО ДНЯМ";
                }
                case OVERALL: {

                    return "ОБЩАЯ";
                }
            }

            return "Error";
        }

        @Override
        public void onPageScrolled(int position,
                                   float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {

                case OVERALL: {

                    mOverallFragment.onFragmentAction(null, -1);
                    break;
                }

                case DAYS: {

                    Log.d("TabPagerAdapter", "onPageSelected - position: "
                            + "DAYS");

                    break;
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
