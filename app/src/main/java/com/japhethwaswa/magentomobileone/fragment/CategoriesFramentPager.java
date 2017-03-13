package com.japhethwaswa.magentomobileone.fragment;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        //initialize cursor loader
        getActivity().getSupportLoaderManager().initLoader(URL_LOADER, null, this);

        //setup the adapter
      categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(cursor);
        fragmentCategoriesPagerBinding.categoriesHomeList.setAdapter(categoriesRecyclerViewAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        fragmentCategoriesPagerBinding.categoriesHomeList.setLayoutManager(llm);

        return fragmentCategoriesPagerBinding.getRoot();
    }

   @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                JumboContract.MainEntry.COLUMN_CATEGORY_ID,
                JumboContract.MainEntry.COLUMN_TITLE,
                JumboContract.MainEntry.COLUMN_IMAGE_URL
        };

        return new CursorLoader(this.getActivity(), JumboContract.MainEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        categoriesRecyclerViewAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoriesRecyclerViewAdapter.setCursor(null);
    }
}
