package com.japhethwaswa.magentomobileone.adapter.recyclerview;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        String regularPrice = cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_PRICE_REGULAR));
        String specialPrice = cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_PRICE_SPECIAL));
        product.setName(cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_NAME)));
        product.setIcon(cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_ICON)));
        product.setPrice_regular(regularPrice);
        product.setPrice_special(specialPrice);

        if(specialPrice != null && !specialPrice.isEmpty()){

            /**remove all non numeric characters**/
            regularPrice = regularPrice +"&*%$";
            String regularPriceFormatted = regularPrice.replaceAll("[^\\d.]","");
            String specialPriceFormatted = specialPrice.replaceAll("[^\\d.]","");
            double regularPriceFormattedNum = (Double.valueOf(regularPriceFormatted));
            double specialPriceFormattedNum = (Double.valueOf(specialPriceFormatted));

            double number = (((regularPriceFormattedNum-specialPriceFormattedNum)/regularPriceFormattedNum)*100);
            int roundedDiscount = (int)Math.round(number);
            product.setDiscount_percentage(String.valueOf(roundedDiscount)+"% OFF");
        }
        categoryProductItemBinding.setProduct(product);

    }

}
