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
import com.japhethwaswa.magentomobileone.databinding.ActivityHomeBinding;
import com.japhethwaswa.magentomobileone.databinding.ActivityProductDetailBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.fragment.CategoriesFramentPager;
import com.japhethwaswa.magentomobileone.fragment.FragmentProductDetailsImages;
import com.japhethwaswa.magentomobileone.fragment.FragmentProductDetailsInfo;
import com.japhethwaswa.magentomobileone.fragment.HomeFragmentPager;
import com.japhethwaswa.magentomobileone.nav.NavMenuManager;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding activityProductDetailBinding;
    private FragmentProductDetailsImages fragmentProductDetailsImages;
    private FragmentProductDetailsInfo fragmentProductDetailsInfo;
    private int entityId;

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

        //get data from the CategoryActivity
        Intent intent = getIntent();
        entityId = intent.getIntExtra("entityId", 0);

        if (savedInstanceState != null) {
            entityId =  savedInstanceState.getInt("entityId");
        }

        //if entitiy id is 0
        if(entityId == 0){
            super.onBackPressed();
            finish();
        }

        setSupportActionBar(activityProductDetailBinding.toolbar);

        getSupportActionBar().setTitle("Product Details");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //fragments management
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentProductDetailsImages = (FragmentProductDetailsImages) fragmentManager.findFragmentById(R.id.fragProductDetailsImages);

        fragmentProductDetailsInfo = (FragmentProductDetailsInfo) fragmentManager.findFragmentById(R.id.fragProductDetailsInfo);

        if (savedInstanceState == null) {
           //send entity id if its the first time and not on orientation change
            fragmentProductDetailsImages.receiveEntityIdentifier(entityId);
            fragmentProductDetailsInfo.receiveEntityIdentifier(entityId);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("savedInstance", 1);
        outState.putInt("entityId", entityId);
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


}
