package com.example.norgaard.barty.Activities.Maps;

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
import com.example.norgaard.barty.R;

import java.util.ArrayList;

public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.BarDistanceAdapterViewHolder> {

    private final Context mContext;
    final private BarDistanceOnClickHandler mClickHandler;
    private ArrayList<Bar> mBarData = new ArrayList<Bar>();

    public interface BarDistanceOnClickHandler {

        void onClick(Bar clickedBar);
    }

    public MapsAdapter(@NonNull Context context, BarDistanceOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public BarDistanceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.bar_maps_item, parent, false);
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

            distance = (TextView) view.findViewById(R.id.barDistanceText);
            barName = (TextView) view.findViewById(R.id.barNameListText);
            barIcon = (ImageView) view.findViewById(R.id.barImage);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Bar clickedBar = mBarData.get(adapterPosition);

            mClickHandler.onClick(clickedBar);
        }
    }

    void swapData(ArrayList<Bar> bars) {
        mBarData.clear();
        mBarData.addAll(bars);
        notifyDataSetChanged();
    }
}
