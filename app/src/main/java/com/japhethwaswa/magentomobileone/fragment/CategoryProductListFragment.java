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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.recyclerview.CategoriesRecyclerViewAdapter;
import com.japhethwaswa.magentomobileone.adapter.recyclerview.CategoryProductsRecyclerViewAdapter;
import com.japhethwaswa.magentomobileone.app.CategoryActivity;
import com.japhethwaswa.magentomobileone.databinding.ActivityCategoryBinding;
import com.japhethwaswa.magentomobileone.databinding.FragmentCategoryProductListBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;
import com.japhethwaswa.magentomobileone.nav.NavMenuManager;
import com.japhethwaswa.magentomobileone.service.JumboWebService;

public class CategoryProductListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    Cursor cursor;
    private static final int URL_LOADER = 9;
    int categoryId;
    private CategoryProductsRecyclerViewAdapter categoryProductsRecyclerViewAdapter;
    private FragmentCategoryProductListBinding fragmentCategoryProductListBinding;
    ActivityCategoryBinding activityCategoryBinding;
    public CategoryActivity catActivity;
    private NavMenuManager navMenuManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //TODO start background job to fetch this category products from magento api
        fragmentCategoryProductListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_product_list, container, false);

        //get the passed category id
        Bundle bundle = getArguments();
        categoryId = bundle.getInt("categoryIdFrag");
        int fromCatActivity = bundle.getInt("fromCatActivity");

        if (savedInstanceState != null) {
            categoryId = savedInstanceState.getInt("categoryIdFrag");
        }

        //get the categoryactivity
        catActivity = (CategoryActivity) getActivity();
        //set toolbar title
        activityCategoryBinding = catActivity.activityCategoryBinding;
        setToolBarCategoryTitle(categoryId);


        //todo remember to save categoryId on screen rotation
        //todo initialize cursor loader after categoryId has been set.


        //todo create custom adapter for the recyclerview
        //todo cursor to fetch filters for spinner ie sub-categories
        //todo have a local variable to store the category filter id and retrieve in onsaveinsance


        //setup the adapter
        categoryProductsRecyclerViewAdapter = new CategoryProductsRecyclerViewAdapter(cursor);
        fragmentCategoryProductListBinding.categoryProductRecycler.setAdapter(categoryProductsRecyclerViewAdapter);

        //update nav menu
        navMenuManager = new NavMenuManager(getActivity());


        //send background job to fetch this category products and sub categories(100 products)
        //jobManager.addJobInBackground(new RetrieveCategoriesProducts(true,null,null,null,null));


        //initialize cursor loader
        getActivity().getSupportLoaderManager().initLoader(URL_LOADER, null, this);
        if (savedInstanceState == null) {
            //restart loader
            getActivity().getSupportLoaderManager().restartLoader(URL_LOADER, null, this);
        }


        //get screen width
        int scrWidth = getScreenDimensions();
        int numItems = 1;

        if (scrWidth >= 800) {
            numItems = 2;
        }
        if (scrWidth >= 1280) {
            numItems = 3;
        }

        if (scrWidth >= 800) {
            GridLayoutManager layoutMgr = new GridLayoutManager(getActivity(), numItems);
            //set Gridlayout manager for the recyclerview
            fragmentCategoryProductListBinding.categoryProductRecycler.setLayoutManager(layoutMgr);
        } else {
            LinearLayoutManager layoutMgr = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            //set Linearlayout manager for the recyclerview
            fragmentCategoryProductListBinding.categoryProductRecycler.setLayoutManager(layoutMgr);

            SnapHelper helper = new LinearSnapHelper();
            helper.attachToRecyclerView(fragmentCategoryProductListBinding.categoryProductRecycler);

        }


        return fragmentCategoryProductListBinding.getRoot();
    }

    private void setToolBarCategoryTitle(int categoryId) {

        final String[] toolbarTitle = new String[1];

        String[] projection = {
                JumboContract.CategoryEntry.COLUMN_LABEL
        };

        String selection = JumboContract.CategoryEntry.COLUMN_ENTITY_ID + "=?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        JumboQueryHandler handler = new JumboQueryHandler(getActivity().getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                while (cursor.moveToNext()) {
                    toolbarTitle[0] = cursor.getString(cursor.getColumnIndex(JumboContract.CategoryEntry.COLUMN_LABEL)).toUpperCase();
                }
                activityCategoryBinding.btnNavToolbarTitle.setText(toolbarTitle[0]);
                cursor.close();
            }

        };

        handler.startQuery(17, null, JumboContract.CategoryEntry.CONTENT_URI, projection, selection, selectionArgs, null);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("categoryIdFrag", categoryId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //todo fetch products using the categoryId provided
        String[] projection = {
                JumboContract.ProductEntry.COLUMN_ICON,
                JumboContract.ProductEntry.COLUMN_NAME,
                JumboContract.ProductEntry.COLUMN_ENTITY_ID,
                JumboContract.ProductEntry.COLUMN_PRICE_REGULAR,
                JumboContract.ProductEntry.COLUMN_PRICE_SPECIAL,
                JumboContract.ProductEntry.COLUMN_ENTITY_TYPE
        };
        //String selection = JumboContract.MainEntry.COLUMN_SECTION + "=?";
        String selection = JumboContract.ProductEntry.COLUMN_CATEGORY_IDS + " LIKE ?";
        //String[] selectionArgs = {"4"};

        String dbCategoryId = String.valueOf(categoryId);
        String[] selectionArgs = {"%-" + dbCategoryId + "-%"};
        String orderBy = null;
        //String orderBy = JumboContract.MainEntry.COLUMN_KEY_HOME;


        return new CursorLoader(this.getActivity(), JumboContract.ProductEntry.CONTENT_URI, projection, selection, selectionArgs, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() > 0) {
            fragmentCategoryProductListBinding.categoryProdsFragPageLoader.stopProgress();
        }
        categoryProductsRecyclerViewAdapter.setCursor(data);


        //update nav menu
        navMenuManager.updateMenu(activityCategoryBinding.layoutNavViewMain.navView, categoryId);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoryProductsRecyclerViewAdapter.setCursor(null);
    }


    public int getScreenDimensions() {
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


    /** public void setActivityLayout(ActivityCategoryBinding activityCategoryBinding) {
     this.activityCategoryBinding = activityCategoryBinding;
     }**/
}
