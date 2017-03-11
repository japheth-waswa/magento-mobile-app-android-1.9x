package com.japhethwaswa.magentomobileone.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.japhethwaswa.magentomobileone.adapter.HomeViewPagerAdapter;
import com.japhethwaswa.magentomobileone.app.HomeActivity;
import com.japhethwaswa.magentomobileone.app.MainActivity;
import com.japhethwaswa.magentomobileone.databinding.FragmentHomePagerBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;
import com.japhethwaswa.magentomobileone.model.HomeVars;

public class HomeFragmentPager extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private FragmentHomePagerBinding fragmentHomePagerBinding;
    private HomeVars homeVars;
    private static final int URL_LOADER = 1;
    private int initialPosition = -1;
    Cursor cursor;
    HomeViewPagerAdapter homeViewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentHomePagerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_pager, container, false);
        getActivity().getSupportLoaderManager().initLoader(URL_LOADER, null, this);

        //restart loader if bundle has content
        if (savedInstanceState != null && savedInstanceState.getInt("resetLoader") == 1) {
            getActivity().getSupportLoaderManager().restartLoader(URL_LOADER, null, this);
            initialPosition = savedInstanceState.getInt("viwePagerCurrent");
        }


        homeVars = new HomeVars();
        fragmentHomePagerBinding.setHomeVars(homeVars);

        //set up the viewpager adapter
        homeViewPagerAdapter = new HomeViewPagerAdapter(cursor);
        fragmentHomePagerBinding.homeViewPager.setAdapter(homeViewPagerAdapter);


        return fragmentHomePagerBinding.getRoot();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("resetLoader", 1);
        outState.putInt("viwePagerCurrent", fragmentHomePagerBinding.homeViewPager.getCurrentItem());
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                JumboContract.MainEntry.COLUMN_CATEGORY_ID,
                JumboContract.MainEntry.COLUMN_PRODUCT_ID,
                JumboContract.MainEntry.COLUMN_UPDATED_AT,
                JumboContract.MainEntry.COLUMN_KEY_HOME,
                JumboContract.MainEntry.COLUMN_IMAGE_URL,
                JumboContract.MainEntry.COLUMN_SECTION
        };

        return new CursorLoader(this.getActivity(), JumboContract.MainEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //ensure you disable the loader only if cursor is null
        //cursor = data;
        if (data.getCount() > 0) {
            //loop through the cursor getting the item
            while (data.moveToNext()) {

                //check if is normal variable
                loadDataToHomeTab(data);


            }
            //activityMainBinding.mainPageLoader.stopProgress();
        }

        //fetch specific data for sliders
        fetchSliderData();

        //homeViewPagerAdapter.setCursor(data);
        //set current item
        //startCountDown();
        /**if(initialPosition != -1){
         Log.e("jeff","set wherever");
         Log.e("jeff",String.valueOf(initialPosition));
         fragmentHomePagerBinding.homeViewPager.setCurrentItem(initialPosition);
         initialPosition = -1;
         }**/


    }

    /**private void startCountDown() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (initialPosition != -1) {
                    Log.e("jeff", "set wherever");
                    Log.e("jeff", String.valueOf(initialPosition));
                    fragmentHomePagerBinding.homeViewPager.setCurrentItem(initialPosition);
                    initialPosition = -1;
                }
            }
        }.start();

    }**/

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        homeViewPagerAdapter.setCursor(null);
    }

    public void loadDataToHomeTab(Cursor data) {

        String imageUrl = data.getString(data.getColumnIndex(JumboContract.MainEntry.COLUMN_IMAGE_URL));
        String keyHome = data.getString(data.getColumnIndex(JumboContract.MainEntry.COLUMN_KEY_HOME));
        String sectionHome = data.getString(data.getColumnIndex(JumboContract.MainEntry.COLUMN_SECTION));
        String catId = data.getString(data.getColumnIndex(JumboContract.MainEntry.COLUMN_CATEGORY_ID));
        String prodId = data.getString(data.getColumnIndex(JumboContract.MainEntry.COLUMN_PRODUCT_ID));

        switch (sectionHome) {
            case "2":
                if (keyHome.equalsIgnoreCase("column_11")) {
                    homeVars.setSectionTwoOne(imageUrl);
                }
                if (keyHome.equalsIgnoreCase("column_12")) {
                    homeVars.setSectionTwoTwo(imageUrl);
                }

                break;
            case "3":
                homeVars.setSectionThree(imageUrl);
                break;
            default:

                break;
        }
    }


    private void fetchSliderData() {

        String[] projection = {
                JumboContract.MainEntry.COLUMN_CATEGORY_ID,
                JumboContract.MainEntry.COLUMN_PRODUCT_ID,
                JumboContract.MainEntry.COLUMN_UPDATED_AT,
                JumboContract.MainEntry.COLUMN_KEY_HOME,
                JumboContract.MainEntry.COLUMN_IMAGE_URL,
                JumboContract.MainEntry.COLUMN_SECTION
        };

        String selection = JumboContract.MainEntry.COLUMN_SECTION + "=?";
        String[] selectionArgs = {"1"};

        JumboQueryHandler handler = new JumboQueryHandler(getActivity().getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                homeViewPagerAdapter.setCursor(cursor);
                cursor.close();
            }

        };

        handler.startQuery(17, null, JumboContract.MainEntry.CONTENT_URI, projection, selection, selectionArgs, JumboContract.MainEntry.COLUMN_KEY_HOME);
    }

    //set current item if view pages already created
    public void PageViewReady() {

        if (initialPosition != -1) {
            Log.e("jeff", "set wherever");
            Log.e("jeff", String.valueOf(initialPosition));
            fragmentHomePagerBinding.homeViewPager.setCurrentItem(initialPosition);
            initialPosition = -1;
        }

    }
}
