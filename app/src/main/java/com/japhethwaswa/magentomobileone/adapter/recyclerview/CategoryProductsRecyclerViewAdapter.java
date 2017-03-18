package com.japhethwaswa.magentomobileone.adapter.recyclerview;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;

public class CategoryProductsRecyclerViewAdapter extends RecyclerView.Adapter<CategoryProductViewHolder> {

    private Cursor cursor;

    public CategoryProductsRecyclerViewAdapter(Cursor cursor) {

        this.cursor = cursor;
    }

    @Override
    public CategoryProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View productContainer = inflater.inflate(R.layout.category_product_item,parent,false);
        return new CategoryProductViewHolder(productContainer);
    }

    @Override
    public void onBindViewHolder(CategoryProductViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.bind(cursor);
    }

    @Override
    public int getItemCount() {
        if(cursor == null){
            return 0;
        }
        return cursor.getCount();
    }

    public void setCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
