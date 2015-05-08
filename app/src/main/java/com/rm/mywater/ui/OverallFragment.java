package com.rm.mywater.ui;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;
import com.rm.mywater.OnFragmentInteractionListener;
import com.rm.mywater.R;
import com.rm.mywater.adapter.DividerItemDecorator;
import com.rm.mywater.model.Drink;
import com.rm.mywater.util.DrinkUtil;
import com.rm.mywater.util.Prefs;
import com.rm.mywater.util.base.BaseFragment;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverallFragment extends BaseFragment implements OnFragmentInteractionListener {

    private static OverallFragment sInstance = new OverallFragment();

    private ArrayList<Drink> mOverallList;
    private ParallaxRecyclerAdapter<Drink> mParallaxAdapter;

    private RecyclerView mOverallRecycler;
    private LinearLayoutManager mLayoutManager;
    private View mHeaderContainer;

    // header
    private PieChart mPieChart;

    public OverallFragment() {
        // Required empty public constructor
    }

    public static OverallFragment getInstance() {
        return sInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mOverallList = Prefs.getOverallDrinks();

        mOverallRecycler = (RecyclerView) view.findViewById(R.id.overall_drinks);

        mLayoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mOverallRecycler.setLayoutManager(mLayoutManager);
        mOverallRecycler.setHasFixedSize(true);
        mOverallRecycler.addItemDecoration(
                new DividerItemDecorator(
                        getActivity(),
                        DividerItemDecorator.VERTICAL_LIST
                )
        );

        mParallaxAdapter = new ParallaxRecyclerAdapter<>(mOverallList);
        mParallaxAdapter.implementRecyclerAdapterMethods(new RecyclerAdapterImplementor());

        mHeaderContainer = getLayoutInflater(savedInstanceState)
                .inflate(R.layout.item_overall_header, mOverallRecycler, false);

        mPieChart = (PieChart) mHeaderContainer.findViewById(R.id.piechart);

        mPieChart.setUseInnerValue(true);
        mPieChart.setInnerPaddingOutline(0);
        mPieChart.setAnimationTime(600);
        mPieChart.setInnerValueString(Prefs.getOverall() + " л");

        for (Drink d : mOverallList) {

            mPieChart.addPieSlice(new PieModel(
                    d.getVolume(),
                    DrinkUtil.getPieColor(d.getType())
            ));
        }

        mParallaxAdapter.setParallaxHeader(mHeaderContainer, mOverallRecycler);

        mOverallRecycler.setAdapter(mParallaxAdapter);
    }

    @Override
    public <T> void onFragmentAction(T data, int key) {


        if (!mPieChart.isShown()) {

            Log.d("OverallFragment", "onFragmentAction - mPieChart.getVisibility(): "
                    + mPieChart.getVisibility());

            mPieChart.setVisibility(View.VISIBLE);
            mPieChart.startAnimation();

//            mPieChart.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//
//                }
//            }, 200);
        }

    }


    private class RecyclerAdapterImplementor
            implements ParallaxRecyclerAdapter.RecyclerAdapterMethods {

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

            Drink drink = mOverallList.get(i);

            int drinkColor = DrinkUtil.getPieColor(drink.getType());

            Log.d("Implementor", "onBindViewHolder - drinkColor: "
                    + drinkColor);

            ((ViewHolder) viewHolder).mDrinkTitle.setText(DrinkUtil.getTitle(drink.getType()));
            ((ViewHolder) viewHolder).mDrinkVol.setText(drink.getVolume() + " л");
            ((ViewHolder) viewHolder).mDrinkIcon.getDrawable().mutate()
                    .setColorFilter(drinkColor, PorterDuff.Mode.MULTIPLY);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            return new ViewHolder(
                    LayoutInflater
                            .from(getActivity())
                            .inflate(R.layout.item_overall, mOverallRecycler, false)
            );
        }

        @Override
        public int getItemCount() {

            return mOverallList.size();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mDrinkTitle;
        public TextView mDrinkVol;
        public ImageView mDrinkIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            mDrinkTitle = (TextView) itemView.findViewById(R.id.overall_drink_title);
            mDrinkVol = (TextView) itemView.findViewById(com.rm.mywater.R.id.overall_vol);
            mDrinkIcon = (ImageView) itemView.findViewById(R.id.overall_icon);
        }
    }
}
