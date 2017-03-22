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
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailInfoBinding;

public class FragmentProductDetailsInfo extends Fragment{

    private FragmentProductDetailInfoBinding fragmentProductDetailInfoBinding;
    //todo we need a cursor loader to load the product details and options from db from database
    //todo initiate bg job to get product options,reviews etc.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentProductDetailInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail_info,container,false);

        return fragmentProductDetailInfoBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
