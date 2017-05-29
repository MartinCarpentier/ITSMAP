package com.example.norgaard.barty.BarSale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.norgaard.barty.Data.BartyContract;
import com.example.norgaard.barty.Models.DrinkBase;
import com.example.norgaard.barty.R;
import com.example.norgaard.barty.Utilities;

/**
 * Created by marti on 28-05-2017.
 */

public class PointOfSaleAdapter extends RecyclerView.Adapter<PointOfSaleAdapter.PointOfSaleViewHolder>{

    private final Context mContext;

    private Cursor cursor = null;

    PointOnSaleOnClickHandler mClickHandler;

    public interface PointOnSaleOnClickHandler {
        void onAddOrDelete(ContentValues values);
        void onDelete(String drinkName);
    }

    public PointOfSaleAdapter(@NonNull Context context, PointOnSaleOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public PointOfSaleAdapter.PointOfSaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.point_of_sale_drink_item, parent, false);

        view.setFocusable(true);

        return new PointOfSaleAdapter.PointOfSaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PointOfSaleAdapter.PointOfSaleViewHolder holder, int position) {
        cursor.moveToPosition(position);

        String drinkName = cursor.getString(PointOfSale.COLUMN_DRINK_NAME);
        int quantity = cursor.getInt(PointOfSale.COLUMN_DRINK_QUANTITY);
        double price = cursor.getDouble(PointOfSale.COLUMN_DRINK_PRICE);
        long barId = cursor.getLong(PointOfSale.COLUMN_FOREIGN_BAR_ID);

        holder.drinkName.setText(drinkName);
        holder.quantity.setText(String.valueOf(quantity));
        holder.totalPrice.setText(String.valueOf(price*quantity));
        holder.singleDrinkPrice = price;
        holder.barId = barId;
    }

    @Override
    public int getItemCount() {
        if(cursor == null)
            return 0;

        return cursor.getCount();
    }


    class PointOfSaleViewHolder extends RecyclerView.ViewHolder {

        public TextView drinkName;
        TextView quantity;
        TextView totalPrice;
        long barId;
        double singleDrinkPrice;

        PointOfSaleViewHolder(View view) {
            super(view);

            drinkName = (TextView) view.findViewById(R.id.drinkNamePOS);
            quantity = (TextView) view.findViewById(R.id.amountOfDrinks);
            totalPrice = (TextView) view.findViewById(R.id.drinkPricePOS);

            FloatingActionButton add = (FloatingActionButton)view.findViewById(R.id.addFabButton);
            FloatingActionButton remove = (FloatingActionButton)view.findViewById(R.id.removeFabButton);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("PointOfSaleAdapter", "add clicked");

                    DrinkBase drink = new DrinkBase();

                    drink.setName(drinkName.getText().toString());
                    drink.setPrice((long)singleDrinkPrice);

                    ContentValues values = Utilities.createContentValuesForDrink(drink, barId, Integer.valueOf(quantity.getText().toString())+1);

                    mClickHandler.onAddOrDelete(values);
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrinkBase drink = new DrinkBase();

                    drink.setName(drinkName.getText().toString());
                    drink.setPrice((long)singleDrinkPrice);

                    ContentValues values = Utilities.createContentValuesForDrink(drink, barId, Integer.valueOf(quantity.getText().toString())-1);


                    if(Integer.valueOf(quantity.getText().toString())-1 == 0)
                    {
                        mClickHandler.onDelete(drink.getName());
                    }
                    else
                    {
                        mClickHandler.onAddOrDelete(values);
                    }

                }
            });
        }
    }

    void swapData(Cursor currentCursor) {
        Log.d("Drinksadapter", "swapping drinks ");
        cursor = currentCursor;
        notifyDataSetChanged();
    }
}