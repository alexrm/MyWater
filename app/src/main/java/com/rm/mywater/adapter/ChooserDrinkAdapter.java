package com.rm.mywater.adapter;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rm.mywater.R;
import com.rm.mywater.model.Drink;
import com.rm.mywater.util.DrinkUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alex
 */
public class ChooserDrinkAdapter extends RecyclerView.Adapter<ChooserDrinkAdapter.ViewHolder> {

    public interface OnItemClickListener {

        <T> void onItemClick(T data, int position);
    }

    private OnItemClickListener mListener;
    private ArrayList<Integer> mDrinkTypeList
            = new ArrayList<>(Arrays.asList(Drink.DRINK_TYPES));

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_chooser_drink, parent, false);

        ViewHolder holder = new ViewHolder(itemView);

        if (mListener != null)
            holder.setOnItemClickListener(mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int drinkType = mDrinkTypeList.get(position);

        holder.mDrinkTitle.setText(
                DrinkUtil.getTitle(drinkType)
        );

        holder.mDrinkIcon.setColorFilter(
                DrinkUtil.getDrinkColor(drinkType),
                PorterDuff.Mode.MULTIPLY
        );

        holder.mDrinkType = mDrinkTypeList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDrinkTypeList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView mDrinkTitle;
        public ImageView mDrinkIcon;
        public int mDrinkType;

        private OnItemClickListener mClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            mDrinkIcon = (ImageView) itemView.findViewById(R.id.chooser_drink_icon);
            mDrinkTitle = (TextView) itemView.findViewById(R.id.chooser_drink_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mClickListener != null)
                this.mClickListener.onItemClick(mDrinkType, getPosition());
        }

        public void setOnItemClickListener(OnItemClickListener listener) {

            mClickListener = listener;
        }
    }
}