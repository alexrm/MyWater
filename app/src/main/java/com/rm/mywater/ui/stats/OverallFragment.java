package com.rm.mywater.ui.stats;


import android.graphics.PorterDuff;
import android.os.Build;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;
import com.rm.mywater.OnFragmentInteractionListener;
import com.rm.mywater.R;
import com.rm.mywater.database.DrinkHistoryDatabase;
import com.rm.mywater.database.OnDataRetrievedListener;
import com.rm.mywater.model.Drink;
import com.rm.mywater.util.DrinkUtil;
import com.rm.mywater.util.base.BaseFragment;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverallFragment extends BaseFragment implements OnFragmentInteractionListener {

    private ArrayList<Drink> mOverallList;
    private ParallaxRecyclerAdapter<Drink> mParallaxAdapter;

    private RelativeLayout mEmptyView;
    private RecyclerView mOverallRecycler;
    private LinearLayoutManager mLayoutManager;
    private View mHeaderContainer;

    private int mOverallVolume;

    private ArrayList<Drink> mRemovableDrinks = new ArrayList<>();

    // header
    private PieChart mPieChart;

    public OverallFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mOverallRecycler = (RecyclerView) view.findViewById(R.id.overall_drinks);
        mEmptyView = (RelativeLayout) view.findViewById(R.id.empty_box);

        mLayoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mOverallRecycler.setLayoutManager(mLayoutManager);
        mOverallRecycler.setHasFixedSize(true);

        DrinkHistoryDatabase.retrieveOverall(getActivity(), new OnDataRetrievedListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onDataReceived(Collection<?> data) {

                mOverallList = (ArrayList<Drink>) data;

                filterDrinksList();

                mHeaderContainer = LayoutInflater
                        .from(getActivity())
                        .inflate(R.layout.item_overall_header, mOverallRecycler, false);

                mPieChart = (PieChart) mHeaderContainer.findViewById(R.id.piechart);

                for (Drink d : mOverallList) {

                    mPieChart.addPieSlice(new PieModel(
                            d.getVolume(),
                            DrinkUtil.getDrinkColor(d.getType())
                    ));
                }

                mPieChart.setUseInnerValue(true);
                mPieChart.setInnerPaddingOutline(0);
                mPieChart.setAnimationTime(600);
                mPieChart.setInnerValueString(mOverallVolume + " л");

                mParallaxAdapter = new ParallaxRecyclerAdapter<>(mOverallList);
                mParallaxAdapter.implementRecyclerAdapterMethods(
                        new RecyclerAdapterImplementor()
                );

                mParallaxAdapter.setParallaxHeader(mHeaderContainer, mOverallRecycler);

                mOverallRecycler.setAdapter(mParallaxAdapter);
            }

            @Override
            public void onError(String err) {

                Log.d("OverallFragment", "onError - error: "
                        + err);

                mOverallRecycler.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public <T> void onFragmentAction(T data, int key) {


        if (!mPieChart.isShown()) {

            Log.d("OverallFragment", "onFragmentAction - mPieChart.getVisibility(): "
                    + mPieChart.getVisibility());

            mPieChart.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mPieChart.startAnimation();
            }
        }

    }

    private void filterDrinksList() {

        mOverallVolume = mOverallList.remove(0).getVolume() / 1000;

        for (Drink d : mOverallList) {

            if (d.getVolume() == 0) {

                mRemovableDrinks.add(d);
            }
        }

        mOverallList.removeAll(mRemovableDrinks);
    }

    private class RecyclerAdapterImplementor
            implements ParallaxRecyclerAdapter.RecyclerAdapterMethods {

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            Drink drink = mOverallList.get(position);

            int drinkColor = DrinkUtil.getDrinkColor(drink.getType());

            Log.d("Implementor", "onBindViewHolder - drinkColor: "
                    + drinkColor);

            ((ViewHolder) viewHolder).mDrinkTitle.setText(DrinkUtil.getTitle(drink.getType()));
            ((ViewHolder) viewHolder).mDrinkVol.setText((float) drink.getVolume()/1000 + " л");
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
