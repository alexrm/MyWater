package com.rm.mywater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rm.mywater.model.Drink;
import com.rm.mywater.util.DrinkUtil;
import com.rm.mywater.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by alex on 22/04/15.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private final OnItemClickListener mListener;
    private ArrayList<Drink> mDrinks = new ArrayList<>();

    public TimelineAdapter(ArrayList<Drink> drinks, OnItemClickListener listener) {

        this.mDrinks = drinks;
        this.mListener = listener;
    }

    @Override
    public TimelineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_timeline, parent, false);

        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(TimelineAdapter.ViewHolder viewHolder, int position) {

        Drink drink = mDrinks.get(position);

        // TODO icon getter

        viewHolder.mVolume.setText(DrinkUtil.getOz(drink.getVolume()) + " унц");
        viewHolder.mTime.setText(TimeUtil.getTime(drink.getTime()));
        viewHolder.mTitle.setText(DrinkUtil.getTitle(drink.getType()));
    }

    @Override
    public int getItemCount() {
        return mDrinks.size();
    }

    public void removeAt(int position) {

        mDrinks.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        public void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView mIcon;
        public TextView mTime;
        public TextView mTitle;
        public TextView mVolume;

        public OnItemClickListener mClickListener;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            this.mIcon = (ImageView) itemView.findViewById(R.id.drink_icon);
            this.mTitle = (TextView) itemView.findViewById(R.id.drink_title);
            this.mTime = (TextView) itemView.findViewById(R.id.drink_time);
            this.mVolume = (TextView) itemView.findViewById(R.id.drink_volume);

            this.mClickListener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            this.mClickListener.onItemClick(getPosition());
        }
    }
}
