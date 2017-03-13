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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private ActivityHomeBinding activityHomeBinding;
    private String[] navItems;

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

        //Snackbar.make(activityHomeBinding.getRoot(),"jefflilcot",Snackbar.LENGTH_LONG).show();

        navItems = new String[]{"jeff", "lilcot"};
        Menu menu = activityHomeBinding.layoutNavViewMain.navView.getMenu();

        SubMenu subMenu = menu.addSubMenu(78,Menu.NONE,Menu.NONE,"jean");
        subMenu.setGroupCheckable(78,true,true);
        //menu.add(78, Menu.NONE, Menu.NONE, "jean");
        //menu.setGroupCheckable(78,true,true);
        for (int i = 1; i <= 3; i++) {
           // menu.add("runtime item "+ i);
            subMenu.add(78, i, i, "runtime item " + i).setIcon(R.drawable.ic_cart);
        }
        for (int i = 0, count = activityHomeBinding.layoutNavViewMain.navView.getChildCount(); i < count; i++) {
            final View child = activityHomeBinding.layoutNavViewMain.navView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }

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
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        /**DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);**/
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
