package com.japhethwaswa.magentomobileone.adapter.recyclerview;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {

    private Cursor cursor;

    public CategoriesRecyclerViewAdapter(Cursor cursor) {

        this.cursor = cursor;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryContainer = inflater.inflate(R.layout.categories_view_pager_item,parent,false);
        return new CategoriesViewHolder(categoryContainer);
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {
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
