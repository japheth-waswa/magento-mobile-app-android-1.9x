package com.japhethwaswa.magentomobileone.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailImagesBinding;
import com.japhethwaswa.magentomobileone.databinding.MainViewPagerItemBinding;
import com.japhethwaswa.magentomobileone.databinding.ProductViewPagerItemBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.model.PreData;

public class ProductViewPagerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Cursor cursor;
    private FragmentProductDetailImagesBinding fragmentProductDetailImagesBinding;


    public ProductViewPagerAdapter(Cursor cursor, FragmentProductDetailImagesBinding fragmentProductDetailImagesBinding) {
        this.cursor = cursor;
        this.fragmentProductDetailImagesBinding = fragmentProductDetailImagesBinding;
    }

    @Override
    public int getCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    @Override
    //checks whether view is associated with object or object is associated with page view or not.
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Context context = container.getContext();
        inflater = LayoutInflater.from(context);
        ProductViewPagerItemBinding itemViewBinding = DataBindingUtil.inflate(inflater, R.layout.product_view_pager_item, container, false);

        //add product_view_pager_item.xml to viewpager
        PreData preData = new PreData();
        cursor.moveToPosition(position);
        preData.setImageUrl(cursor.getString(cursor.getColumnIndex(JumboContract.ProductImagesEntry.COLUMN_IMAGE_URL_BIG)));

        itemViewBinding.setPreData(preData);
        container.addView(itemViewBinding.getRoot());

        return itemViewBinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public void setCursor(Cursor cursor, int initialPosition) {
        this.cursor = cursor;
        notifyDataSetChanged();

        //set the current item in the viewpager
        if (initialPosition != -1) {
            fragmentProductDetailImagesBinding.productImagesViewPager.setCurrentItem(initialPosition);
        }
    }

}
