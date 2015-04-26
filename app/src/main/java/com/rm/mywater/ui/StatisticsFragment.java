package com.rm.mywater.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rm.mywater.R;
import com.rm.mywater.views.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {


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

    private class TabPagerAdapter extends FragmentStatePagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case DAYS:    return new DaysFragment();
                case OVERALL: return new OverallFragment();
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
    }
}
