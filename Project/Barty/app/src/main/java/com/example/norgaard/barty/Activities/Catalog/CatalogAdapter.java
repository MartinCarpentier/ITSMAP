package com.example.norgaard.barty.Activities.Catalog;

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

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.DrinksAdapterViewHolder> {

    private final Context mContext;

    final private CatalogAdapter.DrinksOnClickHandler mClickHandler;

    private ArrayList<DrinkBase> mDrinkData = new ArrayList<DrinkBase>();

    public interface DrinksOnClickHandler {

        void onClick(DrinkBase drink);
    }

    public CatalogAdapter(@NonNull Context context, CatalogAdapter.DrinksOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public CatalogAdapter.DrinksAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_drink_item, parent, false);

        view.setFocusable(true);

        return new CatalogAdapter.DrinksAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CatalogAdapter.DrinksAdapterViewHolder holder, int position) {
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
            drinkPrice = (TextView) view.findViewById(R.id.drinkPriceBar);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            DrinkBase clickedDrink = mDrinkData.get(adapterPosition);

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
