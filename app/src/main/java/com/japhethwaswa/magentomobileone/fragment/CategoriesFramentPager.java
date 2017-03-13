package com.japhethwaswa.magentomobileone.fragment;

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
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.recyclerview.CategoriesRecyclerViewAdapter;
import com.japhethwaswa.magentomobileone.databinding.FragmentCategoriesPagerBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;

public class CategoriesFramentPager extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int URL_LOADER = 3;
    private FragmentCategoriesPagerBinding fragmentCategoriesPagerBinding;
    private CategoriesRecyclerViewAdapter categoriesRecyclerViewAdapter;
    private Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCategoriesPagerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories_pager, container, false);

        //show loader before initializing cursor loader
        fragmentCategoriesPagerBinding.categoryFragPageLoader.startProgress();
        //initialize cursor loader
        getActivity().getSupportLoaderManager().initLoader(URL_LOADER, null, this);

        //setup the adapter
      categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(cursor);
        fragmentCategoriesPagerBinding.categoriesHomeList.setAdapter(categoriesRecyclerViewAdapter);

        /**LinearLayoutManager layoutMgr = new LinearLayoutManager(getActivity());
        layoutMgr.setOrientation(LinearLayoutManager.HORIZONTAL);**/

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

        fragmentCategoriesPagerBinding.categoriesHomeList.setLayoutManager(layoutMgr);

        return fragmentCategoriesPagerBinding.getRoot();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                JumboContract.MainEntry.COLUMN_CATEGORY_ID,
                JumboContract.MainEntry.COLUMN_TITLE,
                JumboContract.MainEntry.COLUMN_IMAGE_URL
        };
        String selection = JumboContract.MainEntry.COLUMN_SECTION + "=?";
        String[] selectionArgs = {"4"};
        String orderBy = JumboContract.MainEntry.COLUMN_KEY_HOME;

        return new CursorLoader(this.getActivity(), JumboContract.MainEntry.CONTENT_URI, projection,selection,selectionArgs,orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.getCount() > 0){
            fragmentCategoriesPagerBinding.categoryFragPageLoader.stopProgress();
        }
        categoriesRecyclerViewAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoriesRecyclerViewAdapter.setCursor(null);
    }
}
