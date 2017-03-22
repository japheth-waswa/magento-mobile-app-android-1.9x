package com.japhethwaswa.magentomobileone.app;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.HomeTextTabsAdapter;
import com.japhethwaswa.magentomobileone.databinding.ActivityHomeBinding;
import com.japhethwaswa.magentomobileone.databinding.ActivityProductDetailBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.fragment.CategoriesFramentPager;
import com.japhethwaswa.magentomobileone.fragment.HomeFragmentPager;
import com.japhethwaswa.magentomobileone.nav.NavMenuManager;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    //private ActivityHomeBinding activityHomeBinding;
    private ActivityProductDetailBinding activityProductDetailBinding;
    private NavMenuManager navMenuManager;
    private static final int URL_LOADER = 0;
//todo edit display of images in this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //StrictMode
        StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(vmPolicy);
        /**==============**/

        super.onCreate(savedInstanceState);

        activityProductDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);


        if (savedInstanceState == null) {
            //initialize loader
            getSupportLoaderManager().initLoader(URL_LOADER, null, this);
        }else{
            //restart loader
            getSupportLoaderManager().restartLoader(URL_LOADER,null,this);
        }

        /**Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);**/


        setSupportActionBar(activityProductDetailBinding.toolbar);

        getSupportActionBar().setTitle("Product Details");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);**/

        /**activityHomeBinding.layoutContentActivityHome.viewPagerHome.setAdapter(homeTextTabsAdapter);
         activityHomeBinding.layoutContentActivityHome.homeTabs.setupWithViewPager(
         activityHomeBinding.layoutContentActivityHome.viewPagerHome
         );
         /**activityHomeBinding.viewPagerHome.setAdapter(homeTextTabsAdapter);
         activityHomeBinding.homeTabs.setupWithViewPager(activityHomeBinding.viewPagerHome);**/
        //todo pass data to the fragments ie product entity_id to load

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("savedInstance", 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_items_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //perfom query to fetch categories with value of my_parent_id=0
        String[] projection = {
                JumboContract.CategoryEntry.COLUMN_LABEL,
                JumboContract.CategoryEntry.COLUMN_ENTITY_ID,
                JumboContract.CategoryEntry.COLUMN_CONTENT_TYPE,
                JumboContract.CategoryEntry.COLUMN_PARENT_ID,
                JumboContract.CategoryEntry.COLUMN_MY_PARENT_ID,
                JumboContract.CategoryEntry.COLUMN_ICON,
                JumboContract.CategoryEntry.COLUMN_MODIFICATION_TIME
        };

        String selection = JumboContract.CategoryEntry.COLUMN_MY_PARENT_ID + "=?";
        String[] selectionArgs = {"0"};

        return new CursorLoader(this, JumboContract.CategoryEntry.CONTENT_URI, projection, selection, selectionArgs,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //initiate categories menu update
        //todo i suggest we remove the loader coz i dont see its essence here
        //todo i doubt if we need a loader
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //do nothing
    }

}
