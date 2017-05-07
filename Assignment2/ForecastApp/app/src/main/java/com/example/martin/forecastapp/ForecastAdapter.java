package com.example.martin.forecastapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        void onClick(long date);
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

        if(mCursor == null)
        {
            holder.highTempView.setText(mMockedData[position]);
            return;
        }
        mCursor.moveToPosition(position);

        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        int weatherImageId;

        holder.highTempView.setText(String.valueOf(mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP)));

    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return mMockedData.length;

        return mCursor.getCount();
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final TextView highTempView;

        ForecastAdapterViewHolder(View view) {
            super(view);

            //iconView = (ImageView) view.findViewById(R.id.weather_icon);
            //dateView = (TextView) view.findViewById(R.id.date);
            //descriptionView = (TextView) view.findViewById(R.id.weather_description);
            //highTempView = (TextView) view.findViewById(R.id.high_temperature);
            //lowTempView = (TextView) view.findViewById(R.id.low_temperature);

            highTempView = (TextView)view.findViewById(R.id.list_item_heigh_textview);

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(dateInMillis);
        }
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    void mockCursor(String[] mockedData) {
        mCursor = null;
        mMockedData = mockedData;
        notifyDataSetChanged();
    }
}
