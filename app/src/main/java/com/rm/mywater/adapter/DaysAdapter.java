package com.rm.mywater.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rm.mywater.R;
import com.rm.mywater.model.Day;
import com.rm.mywater.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by alex on 01/05/15.
 */
public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    private OnItemClickListener mItemClickListener;

    private ArrayList<Day> mDaysList = new ArrayList<>();

    public DaysAdapter(ArrayList<Day> daysList, OnItemClickListener itemClickListener) {
//
//        for (int i = 0; i < 25; i++) {
//
//            mDaysList.add(Day.getDummy());
//        }

        mDaysList = daysList;
        mItemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d("DaysAdapter", "onCreateViewHolder");

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);

        return new ViewHolder(itemView, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Day current = getItem(position);

        int percent = getItem(position).getPercent();

        int valueTo = Math.round(255 * ((float) percent / 100));

        int textColor = (percent > 50) ?
                Color.parseColor("#ffffff") :
                Color.parseColor("#2c94bc");

        holder.mDate.setTextColor(textColor);
        holder.mVolumePercent.setTextColor(textColor);

        holder.mDate.setText(TimeUtil.getDay(current.getStartTime()));
        holder.mVolumePercent.setText(percent + "%");

        holder.mVolumeProgress.setProgress(percent);
        holder.mVolumeProgress.getProgressDrawable().setAlpha(valueTo);

        if (percent == 100) {

            holder.mAchievement.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDaysList.size();
    }

    public Day getItem(int position) {

        return mDaysList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final OnItemClickListener mClickListener;

        public ProgressBar mVolumeProgress;
        public TextView mVolumePercent;
        public TextView mDate;
        public ImageView mAchievement;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            mVolumeProgress = (ProgressBar) itemView.findViewById(R.id.day_progress);
            mDate = (TextView) itemView.findViewById(R.id.day_date);
            mVolumePercent = (TextView) itemView.findViewById(R.id.day_percent);

            mAchievement = (ImageView) itemView.findViewById(R.id.day_achievement);

            this.mClickListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            this.mClickListener.onItemClick(getPosition());
        }
    }
}
