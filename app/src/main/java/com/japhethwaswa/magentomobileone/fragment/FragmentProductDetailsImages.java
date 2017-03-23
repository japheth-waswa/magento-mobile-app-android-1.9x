package com.japhethwaswa.magentomobileone.fragment;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.ProductViewPagerAdapter;
import com.japhethwaswa.magentomobileone.app.CategoryActivity;
import com.japhethwaswa.magentomobileone.app.ProductDetailActivity;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailImagesBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.job.RetrieveCategoriesProducts;
import com.japhethwaswa.magentomobileone.job.RetrieveProductGallery;

public class FragmentProductDetailsImages extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private FragmentProductDetailImagesBinding fragmentProductDetailImagesBinding;
    private int entityId;
    private static final int URL_LOADER = 11;
    public ProductDetailActivity productDetailActivity;
    private ProductViewPagerAdapter productViewpagerAdapter;
    private int initialPosition = -1;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //StrictMode
        StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(vmPolicy);
        /**==============**/


        fragmentProductDetailImagesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail_images, container, false);

        //get activity context
        productDetailActivity = (ProductDetailActivity) getActivity();


        if (savedInstanceState != null) {
            entityId = savedInstanceState.getInt("entityId");
            initialPosition = savedInstanceState.getInt("viwePagerCurrent");
            //restart loader
            getActivity().getSupportLoaderManager().restartLoader(URL_LOADER,null,this);
        }

        //show loader
        fragmentProductDetailImagesBinding.prodImageLoader.startProgress();

        productViewpagerAdapter = new ProductViewPagerAdapter(cursor,fragmentProductDetailImagesBinding);

        fragmentProductDetailImagesBinding.productImagesViewPager.setAdapter(productViewpagerAdapter);

        return fragmentProductDetailImagesBinding.getRoot();
    }

    public void receiveEntityIdentifier(int entityId) {
        this.entityId = entityId;

        //start a bg job to get product images
        productDetailActivity.jobManager.addJobInBackground(new RetrieveProductGallery(String.valueOf(this.entityId)));


        //initialize loader here to get the necessary data
        getActivity().getSupportLoaderManager().initLoader(URL_LOADER, null, this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("entityId", entityId);
        outState.putInt("viwePagerCurrent",fragmentProductDetailImagesBinding.productImagesViewPager.getCurrentItem());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                JumboContract.ProductImagesEntry.COLUMN_IMAGE_URL_BIG,
        };

        String selection = JumboContract.ProductImagesEntry.COLUMN_ENTITY_ID + "=?";
        String[] selectionArgs = {String.valueOf(entityId)};

        return new CursorLoader(getContext(), JumboContract.ProductImagesEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.getCount() > 0){
            //hide loader
            fragmentProductDetailImagesBinding.prodImageLoader.stopProgress();
        }
        productViewpagerAdapter.setCursor(data,initialPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productViewpagerAdapter.setCursor(null,initialPosition);
        resetInitPos();
    }

    private void resetInitPos(){
        initialPosition = -1;
    }
}
