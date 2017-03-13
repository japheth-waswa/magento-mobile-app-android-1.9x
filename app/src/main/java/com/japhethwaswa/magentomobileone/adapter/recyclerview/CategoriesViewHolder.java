package com.japhethwaswa.magentomobileone.adapter.recyclerview;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.japhethwaswa.magentomobileone.databinding.CategoriesViewPagerItemBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.model.HomeData;


public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    private CategoriesViewPagerItemBinding categoriesViewPagerItemBinding;

    public CategoriesViewHolder(View itemView) {
        super(itemView);
        categoriesViewPagerItemBinding = DataBindingUtil.bind(itemView);
    }

    public void bind(Cursor cursor){

        HomeData homeData = new HomeData();

        //get title and image url
        homeData.setTitle(cursor.getString(cursor.getColumnIndex(JumboContract.MainEntry.COLUMN_TITLE)));
        homeData.setImageUrl(cursor.getString(cursor.getColumnIndex(JumboContract.MainEntry.COLUMN_IMAGE_URL)));
        categoriesViewPagerItemBinding.setHomeData(homeData);

    }

}
