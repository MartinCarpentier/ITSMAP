package com.example.norgaard.barty.BarSale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.norgaard.barty.Models.DrinkBase;
import com.example.norgaard.barty.R;

import java.util.ArrayList;
import java.util.List;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.DrinksAdapterViewHolder> {

    private final Context mContext;

    final private DrinksAdapter.DrinksOnClickHandler mClickHandler;

    private ArrayList<DrinkBase> mDrinkData = new ArrayList<DrinkBase>();

    public interface DrinksOnClickHandler {
        void onClick(DrinkBase drink);
    }

    public DrinksAdapter(@NonNull Context context, DrinksAdapter.DrinksOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public DrinksAdapter.DrinksAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_drink_item, parent, false);

        view.setFocusable(true);

        return new DrinksAdapter.DrinksAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrinksAdapter.DrinksAdapterViewHolder holder, int position) {
        DrinkBase drink = mDrinkData.get(position);

        Glide.with(mContext)
                .load(drink.getImageURL())
                .into(holder.drinkImage);

        holder.drinkName.setText(drink.getName());
        holder.drinkPrice.setText(String.valueOf(drink.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mDrinkData.size();
    }

    class DrinksAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView drinkImage;
        TextView drinkName;
        TextView drinkPrice;

        DrinksAdapterViewHolder(View view) {
            super(view);

            drinkImage = (ImageView) view.findViewById(R.id.drinkImage);
            drinkName = (TextView) view.findViewById(R.id.drinkName);
            drinkPrice = (TextView)view.findViewById(R.id.drinkPriceBar);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            DrinkBase clickedDrink = mDrinkData.get(adapterPosition);

            Toast.makeText(v.getContext(),
                    "OnClick drink click",
                    Toast.LENGTH_SHORT).show();

            //mCursor.moveToPosition(adapterPosition);

            //ImageView weatherIcon =  (ImageView)v.findViewById(R.id.weatherIconImage);
            //TextView high = (TextView)v.findViewById(R.id.list_item_high_textview);
            //TextView low = (TextView)v.findViewById(R.id.list_item_low_textview);

            //long date = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(clickedDrink);
        }
    }

    void swapData(List<? extends DrinkBase> drinks) {
        Log.d("Drinksadapter", "swapping drinks " + drinks.size());
        mDrinkData.clear();
        mDrinkData.addAll(drinks);
        notifyDataSetChanged();
    }
}
