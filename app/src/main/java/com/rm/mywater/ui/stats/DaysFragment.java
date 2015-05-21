package com.rm.mywater.ui.stats;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rm.mywater.OnFragmentInteractionListener;
import com.rm.mywater.R;
import com.rm.mywater.adapter.DaysAdapter;
import com.rm.mywater.adapter.OnItemClickListener;
import com.rm.mywater.database.DrinkHistoryDatabase;
import com.rm.mywater.database.OnDataRetrievedListener;
import com.rm.mywater.model.Day;
import com.rm.mywater.util.base.BaseFragment;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 */
public class DaysFragment extends BaseFragment implements OnItemClickListener {

    private OnFragmentInteractionListener mInteractionListener;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mDays;
    private DaysAdapter mDaysAdapter;
    private ArrayList<Day> mDaysList;

    public DaysFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_days, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mDays = (RecyclerView) view.findViewById(R.id.days_list);
        mDays.setLayoutManager(mLayoutManager);
        mDays.setHasFixedSize(true);

        DrinkHistoryDatabase.retrieveDays(getActivity(), new OnDataRetrievedListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onDataReceived(Collection<?> data) {

                mDaysList = (ArrayList<Day>) data;

                mDaysAdapter = new DaysAdapter(mDaysList, DaysFragment.this);

                mDays.setAdapter(mDaysAdapter);
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setInteractionListener((OnFragmentInteractionListener) activity);
    }

    public void setInteractionListener(OnFragmentInteractionListener interactionListener) {
        mInteractionListener = interactionListener;
    }

    @Override
    public void onItemClick(int position) {

        if (mInteractionListener != null) {

            mInteractionListener.onFragmentAction(
                    mDaysList.get(position).getStartTime(),
                    OnFragmentInteractionListener.KEY_OPEN_DAY
            );
        }
    }
}
