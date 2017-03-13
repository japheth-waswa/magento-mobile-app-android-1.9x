package com.japhethwaswa.magentomobileone.app;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.HomeTextTabsAdapter;
import com.japhethwaswa.magentomobileone.databinding.ActivityHomeBinding;
import com.japhethwaswa.magentomobileone.databinding.ContentActivityHomeBinding;
import com.japhethwaswa.magentomobileone.fragment.CategoriesFramentPager;
import com.japhethwaswa.magentomobileone.fragment.HomeFragmentPager;
import com.japhethwaswa.magentomobileone.nav.NavMenuManager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private ActivityHomeBinding activityHomeBinding;
    private NavMenuManager navMenuManager;

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

        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        //initialise();
        prepareDataSource();

        /**Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);**/

        setSupportActionBar(activityHomeBinding.toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, activityHomeBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        activityHomeBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        /** NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);**/
        activityHomeBinding.layoutNavViewMain.navView.setNavigationItemSelectedListener(this);


        HomeTextTabsAdapter homeTextTabsAdapter = new HomeTextTabsAdapter(getSupportFragmentManager(), fragmentList, titleList);

        /** activityHomeBinding.layoutNavToolbar.viewPagerHome.setAdapter(homeTextTabsAdapter);
         activityHomeBinding.layoutNavToolbar.homeTabs.setupWithViewPager(
         activityHomeBinding.layoutNavToolbar.viewPagerHome
         );**/
        activityHomeBinding.viewPagerHome.setAdapter(homeTextTabsAdapter);
        activityHomeBinding.homeTabs.setupWithViewPager(
                activityHomeBinding.viewPagerHome
        );
        /**activityHomeBinding.layoutContentActivityHome.viewPagerHome.setAdapter(homeTextTabsAdapter);
         activityHomeBinding.layoutContentActivityHome.homeTabs.setupWithViewPager(
         activityHomeBinding.layoutContentActivityHome.viewPagerHome
         );
         /**activityHomeBinding.viewPagerHome.setAdapter(homeTextTabsAdapter);
         activityHomeBinding.homeTabs.setupWithViewPager(activityHomeBinding.viewPagerHome);**/

        //TODO on load manager finish activate menu update
        navMenuManager = new NavMenuManager(this);
        navMenuManager.updateMenu(activityHomeBinding.layoutNavViewMain.navView);


    }


    private void prepareDataSource() {

        addDataSource(new HomeFragmentPager(), "Home");
        addDataSource(new CategoriesFramentPager(), "Categories");

    }

    private void addDataSource(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (activityHomeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
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
        Log.e("jeff-waswa", String.valueOf(id));


        /**DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);**/
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
