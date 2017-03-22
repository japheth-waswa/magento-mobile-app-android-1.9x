package com.japhethwaswa.magentomobileone.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailImagesBinding;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailInfoBinding;

public class FragmentProductDetailsInfo extends Fragment{

    private FragmentProductDetailInfoBinding fragmentProductDetailInfoBinding;
    private int entityId;
    private static final int URL_LOADER = 11;
    //todo we need a cursor loader to load the product details and options from db from database
    //todo initiate bg job to get product options,reviews etc.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentProductDetailInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail_info,container,false);


        if (savedInstanceState == null) {
            //do something here
        }else{
            entityId =  savedInstanceState.getInt("entityId");
            //restart loader
            //getSupportLoaderManager().restartLoader(URL_LOADER,null,this);
        }

        return fragmentProductDetailInfoBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("entityId", entityId);
    }

    public void receiveEntityIdentifier(int entityId) {
        this.entityId =  entityId;

        //todo start a bg job in another method to get product images


        //todo initialize loader here to get the necessary data
        //initialize loader
        //getSupportLoaderManager().initLoader(URL_LOADER, null, this);
    }
}
