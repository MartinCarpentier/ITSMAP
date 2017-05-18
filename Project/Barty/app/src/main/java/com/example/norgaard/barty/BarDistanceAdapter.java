package com.example.norgaard.barty;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.norgaard.barty.Models.Bar;

import java.util.ArrayList;

/**
 * Created by marti on 14-05-2017.
 */

public class BarDistanceAdapter extends RecyclerView.Adapter<BarDistanceAdapter.BarDistanceAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private final Context mContext;

    final private BarDistanceOnClickHandler mClickHandler;

    private ArrayList<Bar> mBarData = new ArrayList<Bar>();

    public interface BarDistanceOnClickHandler {
        void onClick(TextView barname);
    }

    public BarDistanceAdapter(@NonNull Context context, BarDistanceOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public BarDistanceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;

        View view = LayoutInflater.from(mContext).inflate(R.layout.bar_item_distance, parent, false);

        view.setFocusable(true);

        return new BarDistanceAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BarDistanceAdapterViewHolder holder, int position) {
        Bar barAtPosition = mBarData.get(position);

        holder.barName.setText(barAtPosition.getBarname());

        //Set baricon
        Glide.with(mContext)
                .load(barAtPosition.getBarlogo())
                .into(holder.barIcon);
    }

    @Override
    public int getItemCount() {
        return mBarData.size();
    }

    class BarDistanceAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView distance;
        private final TextView barName;
        private final ImageView barIcon;

        BarDistanceAdapterViewHolder(View view) {
            super(view);

            distance = (TextView)view.findViewById(R.id.barDistanceText);
            barName = (TextView)view.findViewById(R.id.barNameListText);
            barIcon = (ImageView)view.findViewById(R.id.barImage);

        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();

            //mCursor.moveToPosition(adapterPosition);

            //ImageView weatherIcon =  (ImageView)v.findViewById(R.id.weatherIconImage);
            //TextView high = (TextView)v.findViewById(R.id.list_item_high_textview);
            //TextView low = (TextView)v.findViewById(R.id.list_item_low_textview);

            //long date = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(barName);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    void swapData(ArrayList<Bar> bars) {
        mBarData.clear();
        mBarData.addAll(bars);
        notifyDataSetChanged();
    }
}
