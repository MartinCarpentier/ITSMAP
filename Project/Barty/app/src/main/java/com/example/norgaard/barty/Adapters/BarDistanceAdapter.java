package com.example.norgaard.barty.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.norgaard.barty.Models.BarDistance;
import com.example.norgaard.barty.R;

/**
 * Created by marti on 14-05-2017.
 */

public class BarDistanceAdapter extends RecyclerView.Adapter<BarDistanceAdapter.BarDistanceAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private final Context mContext;
    private String[] mMockedData = new String[0];

    final private BarDistanceOnClickHandler mClickHandler;

    private Cursor mCursor;

    public interface BarDistanceOnClickHandler {
        void onClick(long weatherId, ImageView weatherIcon, TextView high, TextView low);
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


    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return mMockedData.length;

        return mCursor.getCount();
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
            mCursor.moveToPosition(adapterPosition);

            //ImageView weatherIcon =  (ImageView)v.findViewById(R.id.weatherIconImage);
            //TextView high = (TextView)v.findViewById(R.id.list_item_high_textview);
            //TextView low = (TextView)v.findViewById(R.id.list_item_low_textview);

            //long date = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            //mClickHandler.onClick(date, weatherIcon,high, low);
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

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
