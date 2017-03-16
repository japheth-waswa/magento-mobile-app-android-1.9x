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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.HomeTextTabsAdapter;
import com.japhethwaswa.magentomobileone.databinding.ActivityCategoryBinding;
import com.japhethwaswa.magentomobileone.databinding.ActivityHomeBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.fragment.CategoriesFramentPager;
import com.japhethwaswa.magentomobileone.fragment.CategoryProductListFragment;
import com.japhethwaswa.magentomobileone.fragment.HomeFragmentPager;
import com.japhethwaswa.magentomobileone.nav.NavMenuManager;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    public ActivityCategoryBinding activityCategoryBinding;
    private NavMenuManager navMenuManager;
    private static final int URL_LOADER = 0;
    private int categoryId;
    Cursor cursor;


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

        //get data from the HomeActivity
        Intent intent = getIntent();
        categoryId = intent.getIntExtra("categoryId", 0);

        //if categoryId =0 return to the previous activity
        if (categoryId == 0) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
            finish();
        }


        activityCategoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_category);

        //initialize loader
        getSupportLoaderManager().initLoader(URL_LOADER, null, this);

        setSupportActionBar(activityCategoryBinding.toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, activityCategoryBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        activityCategoryBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        /** NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);**/
        activityCategoryBinding.layoutNavViewMain.navView.setNavigationItemSelectedListener(this);


        //TODO on load manager finish activate menu update
        navMenuManager = new NavMenuManager(this);
        //navMenuManager.updateMenu(activityHomeBinding.layoutNavViewMain.navView);

        if (savedInstanceState == null) {
            //start fragment
            startCatFragment();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("savedInstance", 1);
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (activityCategoryBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityCategoryBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        categoryId = id;

        //start a fragment to load items in this category
        startCatFragment();

        /**DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);**/
        activityCategoryBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //todo perform query to fetch categories
        String[] projection = {
                JumboContract.MainEntry.COLUMN_CATEGORY_ID,
                JumboContract.MainEntry.COLUMN_PRODUCT_ID,
                JumboContract.MainEntry.COLUMN_UPDATED_AT,
                JumboContract.MainEntry.COLUMN_KEY_HOME,
                JumboContract.MainEntry.COLUMN_IMAGE_URL,
                JumboContract.MainEntry.COLUMN_SECTION
        };

        return new CursorLoader(this, JumboContract.MainEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.cursor = data;
        //initiate categories menu update
        navMenuManager.updateMenuDeal(data, activityCategoryBinding.layoutNavViewMain.navView);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //todo reset the cursor or do nothing
    }

    public void startCatFragment() {


        FragmentManager fragmentManager = getSupportFragmentManager();
        CategoryProductListFragment categoryProductListFragment = new CategoryProductListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("categoryIdFrag", categoryId);
        categoryProductListFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //unique

        fragmentTransaction.replace(R.id.categoriesProdListFragment, categoryProductListFragment, "catProdFragment");
        fragmentTransaction.commit();
    }


}
