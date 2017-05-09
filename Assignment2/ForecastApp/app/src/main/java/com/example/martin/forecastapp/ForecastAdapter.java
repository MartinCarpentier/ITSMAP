package com.example.martin.forecastapp;

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

import com.example.martin.forecastapp.utils.SunshineDateUtils;
import com.example.martin.forecastapp.utils.Utilities;
import com.example.martin.forecastapp.utils.WeatherIdUtils;

import java.util.Calendar;

/**
 * Created by mbc on 05-05-2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private final Context mContext;
    private String[] mMockedData = new String[0];

    final private ForecastAdapterOnClickHandler mClickHandler;

    private Cursor mCursor;

    public interface ForecastAdapterOnClickHandler {
        void onClick(long weatherId, ImageView weatherIcon, TextView high, TextView low);
    }

    public ForecastAdapter(@NonNull Context context, ForecastAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;

        switch (viewType) {

            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_forecast_today;
                break;
            }

            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item_forecast;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if(mCursor == null)
        {
            holder.highTempView.setText(mMockedData[position]);
            return;
        }
        mCursor.moveToPosition(position);

        holder.highTempView.setText(String.valueOf(mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP)));
        holder.lowTempView.setText(String.valueOf(mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP)));

        long dateAsLong = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String date = SunshineDateUtils.getFriendlyDateString(mContext, dateAsLong, true);
        holder.date.setText(date);

        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);

        boolean isDay = Utilities.checkIfDay(dateAsLong);
        int weatherIconId = WeatherIdUtils.getDrawableIdForWeatherCondition(weatherId,isDay);
        String description = WeatherIdUtils.getStringForWeatherCondition(mContext, weatherId);

        holder.weatherIcon.setImageDrawable(ContextCompat.getDrawable(mContext, weatherIconId));
        holder.shortDescription.setText(description);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return mMockedData.length;

        return mCursor.getCount();
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView lowTempView;
        private final TextView highTempView;
        private final TextView date;
        private final TextView shortDescription;
        private final ImageView weatherIcon;

        ForecastAdapterViewHolder(View view) {
            super(view);

            highTempView = (TextView)view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView)view.findViewById(R.id.list_item_low_textview);
            date = (TextView)view.findViewById(R.id.dateViewId);
            shortDescription = (TextView)view.findViewById(R.id.list_shortDescriptionText);
            weatherIcon = (ImageView)view.findViewById(R.id.weatherIconImage);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            ImageView weatherIcon =  (ImageView)v.findViewById(R.id.weatherIconImage);
            TextView high = (TextView)v.findViewById(R.id.list_item_high_textview);
            TextView low = (TextView)v.findViewById(R.id.list_item_low_textview);

            long date = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(date, weatherIcon,high, low);
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
