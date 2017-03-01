package com.japhethwaswa.magentomobileone.app;

import android.databinding.DataBindingUtil;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.HomeTextTabsAdapter;
import com.japhethwaswa.magentomobileone.databinding.ActivityHomeBinding;
import com.japhethwaswa.magentomobileone.fragment.CategoriesFramentPager;
import com.japhethwaswa.magentomobileone.fragment.HomeFragmentPager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

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

        ActivityHomeBinding activityHomeBinding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        //initialise();
        prepareDataSource();

        HomeTextTabsAdapter homeTextTabsAdapter =  new HomeTextTabsAdapter(getSupportFragmentManager(),fragmentList,titleList);
        activityHomeBinding.viewPagerHome.setAdapter(homeTextTabsAdapter);
        activityHomeBinding.homeTabs.setupWithViewPager(activityHomeBinding.viewPagerHome);


    }


    private void prepareDataSource() {

        addDataSource(new HomeFragmentPager(),"Home");
        addDataSource(new CategoriesFramentPager(),"Categories");

    }

    private void addDataSource(Fragment fragment,String title){
        fragmentList.add(fragment);
        titleList.add(title);
    }


}
