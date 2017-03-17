package com.japhethwaswa.magentomobileone.fragment;


import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.recyclerview.CategoriesRecyclerViewAdapter;
import com.japhethwaswa.magentomobileone.app.CategoryActivity;
import com.japhethwaswa.magentomobileone.databinding.ActivityCategoryBinding;
import com.japhethwaswa.magentomobileone.databinding.FragmentCategoryProductListBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.service.JumboWebService;

public class CategoryProductListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    Cursor cursor;
    private static final int URL_LOADER = 9;
    int categoryId;
    private CategoriesRecyclerViewAdapter categoriesRecyclerViewAdapter;
    private FragmentCategoryProductListBinding fragmentCategoryProductListBinding;
    ActivityCategoryBinding activityCategoryBinding;
    public CategoryActivity catActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //TODO start background job to fetch this category products from magento api
        fragmentCategoryProductListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_product_list,container,false);

        //get the passed category id
        Bundle bundle =  getArguments();
        categoryId = bundle.getInt("categoryIdFrag");

        if(savedInstanceState != null ){
            categoryId = savedInstanceState.getInt("categoryIdFrag");
        }

        //todo use the cursor to get the category name and update in toolbar-use background thread
        //set toolbar title
        catActivity = (CategoryActivity) getActivity();
        activityCategoryBinding = catActivity.activityCategoryBinding;
        activityCategoryBinding.btnNavToolbarTitle.setText("Jeff Lilcot");

        //todo remember to save categoryId on screen rotation
        //todo initialize cursor loader after categoryId has been set.


        //todo create custom adapter for the recyclerview

        //todo set the category name in toolbar appropriate after fetching from db(note on screen rotation)
        //activityCategoryBinding.btnNavToolbar.setTitle("jeff lilcot");

        //setup the adapter
        categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(cursor);
        fragmentCategoryProductListBinding.categoryProductRecycler.setAdapter(categoriesRecyclerViewAdapter);

        //initialize cursor loader
        getActivity().getSupportLoaderManager().initLoader(URL_LOADER, null, this);

        //get screen width
        int  scrWidth = getScreenDimensions();
        int numItems = 1;

        if(scrWidth >= 800 ){
            numItems = 2;
        }
        if(scrWidth >= 1280 ){
            numItems = 3;
        }
        GridLayoutManager layoutMgr = new GridLayoutManager(getActivity(),numItems);

        //set layout manager for the recyclerview
        fragmentCategoryProductListBinding.categoryProductRecycler.setLayoutManager(layoutMgr);

        //perfom xmlconnect tests
        xmlTests();

    return fragmentCategoryProductListBinding.getRoot();
    }

    private void xmlTests() {
        //Log.e("jeff-waswa","xml tests come here");
        JumboWebService.serviceRetrieveCategories(getContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("categoryIdFrag",categoryId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //todo fetch products using the categoryId provided
        String[] projection = {
                JumboContract.MainEntry.COLUMN_CATEGORY_ID,
                JumboContract.MainEntry.COLUMN_TITLE,
                JumboContract.MainEntry.COLUMN_IMAGE_URL
        };
        //String selection = JumboContract.MainEntry.COLUMN_SECTION + "=?";
        String selection = null;
        //String[] selectionArgs = {"4"};
        String[] selectionArgs = null;
        String orderBy = JumboContract.MainEntry.COLUMN_KEY_HOME;

        return new CursorLoader(this.getActivity(), JumboContract.MainEntry.CONTENT_URI, projection,selection,selectionArgs,orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.getCount() > 0){
            fragmentCategoryProductListBinding.categoryProdsFragPageLoader.stopProgress();
        }
        categoriesRecyclerViewAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoriesRecyclerViewAdapter.setCursor(null);
    }


    public int getScreenDimensions(){
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        /**int height = dm.heightPixels;
         int dens = dm.densityDpi;
         double wi = (double)width / (double)dens;
         double hi = (double)height / (double)dens;
         double x = Math.pow(wi,2);
         double y = Math.pow(hi,2);
         double screenInches = Math.sqrt(x+y);**/
        return width;
    }


    public void setActivityLayout(ActivityCategoryBinding activityCategoryBinding) {
        this.activityCategoryBinding = activityCategoryBinding;
    }
}
