package com.japhethwaswa.magentomobileone.adapter.recyclerview;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.japhethwaswa.magentomobileone.databinding.CategoriesViewPagerItemBinding;
import com.japhethwaswa.magentomobileone.databinding.CategoryProductItemBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.model.HomeData;
import com.japhethwaswa.magentomobileone.model.Product;


public class CategoryProductViewHolder extends RecyclerView.ViewHolder {

    private CategoryProductItemBinding categoryProductItemBinding;

    public CategoryProductViewHolder(View itemView) {
        super(itemView);
        categoryProductItemBinding = DataBindingUtil.bind(itemView);
    }

    public void bind(Cursor cursor){

        Product product = new Product();

        //get title and image url
        product.setName(cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_NAME)));
        product.setIcon(cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_ICON)));
        categoryProductItemBinding.setProduct(product);

    }

}
