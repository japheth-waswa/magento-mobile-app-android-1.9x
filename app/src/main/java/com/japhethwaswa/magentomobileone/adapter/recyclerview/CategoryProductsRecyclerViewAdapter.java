package com.japhethwaswa.magentomobileone.adapter.recyclerview;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.fragment.CategoryProductListFragment;

public class CategoryProductsRecyclerViewAdapter extends RecyclerView.Adapter<CategoryProductViewHolder> {

    private Cursor cursor;
    private CategoryProductListFragment categoryProductListFragment;

    public CategoryProductsRecyclerViewAdapter(Cursor cursor, CategoryProductListFragment categoryProductListFragment) {

        this.cursor = cursor;
        this.categoryProductListFragment = categoryProductListFragment;
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

        //official way wait for them to hit the end of items and send bg job
        /**if(position == cursor.getCount()-1){
            categoryProductListFragment.recyclerLastItem(position);

        }**/

        //sneaky way send bg job when they hit the 5th-last item
        if(position == cursor.getCount()-6){
            categoryProductListFragment.recyclerLastItem((cursor.getCount()-1));

        }else if(position == cursor.getCount()-1){
            categoryProductListFragment.recyclerLastItem((cursor.getCount()-1));
        }
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
        this.notifyDataSetChanged();
    }


}
