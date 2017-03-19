package com.japhethwaswa.magentomobileone.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.birbit.android.jobqueue.JobManager;
import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.SpinnerCategoryListAdapter;
import com.japhethwaswa.magentomobileone.adapter.recyclerview.CategoriesRecyclerViewAdapter;
import com.japhethwaswa.magentomobileone.adapter.recyclerview.CategoryProductsRecyclerViewAdapter;
import com.japhethwaswa.magentomobileone.app.CategoryActivity;
import com.japhethwaswa.magentomobileone.databinding.ActivityCategoryBinding;
import com.japhethwaswa.magentomobileone.databinding.FragmentCategoryProductListBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;
import com.japhethwaswa.magentomobileone.job.RetrieveCategories;
import com.japhethwaswa.magentomobileone.job.RetrieveCategoriesProducts;
import com.japhethwaswa.magentomobileone.job.builder.MyJobsBuilder;
import com.japhethwaswa.magentomobileone.model.Category;
import com.japhethwaswa.magentomobileone.model.CategoryList;
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
    private int recyclerViewLastItemPosition;
    static final int ALL_CATEGORY_FILTERS = -1;
    CategoryList categoryList = new CategoryList();
    SpinnerCategoryListAdapter spinnerCategoryListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StrictMode
        StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(vmPolicy);
        /**==============**/

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //TODO start background job to fetch this category products from magento api(done)
        fragmentCategoryProductListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_product_list, container, false);

        //get the passed category id
        Bundle bundle = getArguments();
        categoryId = bundle.getInt("categoryIdFrag");
        int fromCatActivity = bundle.getInt("fromCatActivity");

        if (savedInstanceState != null) {
            categoryId = savedInstanceState.getInt("categoryIdFrag");
            recyclerViewLastItemPosition = savedInstanceState.getInt("recyclerViewLastItemPosition");
        }

        //get the categoryactivity
        catActivity = (CategoryActivity) getActivity();
        //set toolbar title
        activityCategoryBinding = catActivity.activityCategoryBinding;
        setToolBarCategoryTitle(categoryId);


        if (savedInstanceState == null) {

            Resources res = getActivity().getResources();
            String offsetAtCategory = String.valueOf((Integer.valueOf(res.getString(R.string.jumbo_product_count))));
            //send background job to fetch this category products and sub categories(20 products)-only if is first time
            catActivity.jobManager.addJobInBackground(new RetrieveCategoriesProducts(false, String.valueOf(categoryId), String.valueOf(categoryId), "20", offsetAtCategory));

            //set recyclerview expected last item position
            recyclerViewLastItemPosition = Integer.valueOf(offsetAtCategory)-1;
        }

        //todo remember to save categoryId on screen rotation(done)
        //todo initialize cursor loader after categoryId has been set(done)


        //todo create custom adapter for the recyclerview(done)
        //todo cursor to fetch filters for spinners ie sub-categories
        //todo have a local variable to store the category filter id and retrieve in onsaveinsance


        //setup the adapter
        categoryProductsRecyclerViewAdapter = new CategoryProductsRecyclerViewAdapter(cursor,this);
        fragmentCategoryProductListBinding.categoryProductRecycler.setAdapter(categoryProductsRecyclerViewAdapter);

        //update nav menu only once for each category load to prevent ui errors
        navMenuManager = new NavMenuManager(getActivity());
        navMenuManager.updateMenu(activityCategoryBinding.layoutNavViewMain.navView, categoryId);

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

        //set category filters
        setFilterCategories();

        return fragmentCategoryProductListBinding.getRoot();
    }


    //get categories for filter
    private void setFilterCategories() {
        //get cursor with all filter categories
        final JumboQueryHandler handler = new JumboQueryHandler(getContext().getContentResolver()){
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

                if(cursor != null && cursor.getCount() > 0){
                    activityCategoryBinding.categorySpinner.setVisibility(View.VISIBLE);
                    int i = 0;
                    categoryList.ItemList.add(i,new Category(ALL_CATEGORY_FILTERS,"All Categories"));
                    i++;
                    while(cursor.moveToNext()){
                        int entityid = Integer.valueOf(cursor.getString(cursor.getColumnIndex(JumboContract.CategoryEntry.COLUMN_ENTITY_ID)));
                        String  label = cursor.getString(cursor.getColumnIndex(JumboContract.CategoryEntry.COLUMN_LABEL));
                        categoryList.ItemList.add(i,new Category(entityid,label));
                        i++;
                    }
                }else{
                    activityCategoryBinding.categorySpinner.setVisibility(View.INVISIBLE);
                }
                cursor.close();

                if(categoryList.ItemList.size() >0){
                    spinnerCategoryListAdapter =  null;
                    spinnerCategoryListAdapter = new SpinnerCategoryListAdapter(categoryList.ItemList);
                    activityCategoryBinding.categorySpinner.setAdapter(spinnerCategoryListAdapter);
                }


            }
        };
        String[] projection = {
                JumboContract.CategoryEntry.COLUMN_LABEL,
                JumboContract.CategoryEntry.COLUMN_ENTITY_ID
        };

        String selection = JumboContract.CategoryEntry.COLUMN_MY_PARENT_ID + "=?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        handler.startQuery(45,null,JumboContract.CategoryEntry.CONTENT_URI,projection,selection,selectionArgs,null);
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
        outState.putInt("recyclerViewLastItemPosition", recyclerViewLastItemPosition);
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
            Log.e("jeff-waswa-count",String.valueOf(data.getCount()));
            fragmentCategoryProductListBinding.categoryProdsFragPageLoader.stopProgress();
        }
        categoryProductsRecyclerViewAdapter.setCursor(data);

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

    public void recyclerLastItem(int recyclerViewLastItemPositionCurrent){

        //before setting ensure its larger than the current itemPosition and update
        if(recyclerViewLastItemPositionCurrent >= recyclerViewLastItemPosition){

            //set new offset
            String currentItemsOffset = String.valueOf((recyclerViewLastItemPositionCurrent+1));

            //set new expected recyclerview last item
            recyclerViewLastItemPosition = recyclerViewLastItemPositionCurrent+20;

            //then do bg job
            //send background job to fetch this category products and sub categories(20 products)-only if is first time
            catActivity.jobManager.addJobInBackground(new RetrieveCategoriesProducts(false, String.valueOf(categoryId), String.valueOf(categoryId), "20", currentItemsOffset));

        }

    }
}
