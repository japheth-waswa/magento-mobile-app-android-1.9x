package com.japhethwaswa.magentomobileone.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailImagesBinding;

public class FragmentProductDetailsImages extends Fragment{

    private FragmentProductDetailImagesBinding fragmentProductDetailImagesBinding;
    //todo we need a cursor loader to load these images for viewpager from database
    //todo initiate bg job to fetch product images and save their link to the database

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentProductDetailImagesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail_images,container,false);

        return fragmentProductDetailImagesBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
