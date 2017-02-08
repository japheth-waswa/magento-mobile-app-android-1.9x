package com.japhethwaswa.magentomobileone.app;

import android.databinding.DataBindingUtil;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.MainViewPagerAdapter;
import com.japhethwaswa.magentomobileone.databinding.ActivityMainBinding;
import com.japhethwaswa.magentomobileone.model.PreData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


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
        //setContentView(R.layout.activity_main);

        //inflate layout
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        List<PreData> pagerItem = getPreData();

        //ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this,pagerItem);

        //viewPager.setAdapter(mainViewPagerAdapter);
        activityMainBinding.mainViewPager.setAdapter(mainViewPagerAdapter);

    }

    //bg thread to fetch data from an endpoint.
    public List<PreData> getPreData(){

        ArrayList<PreData> preDataList = new ArrayList<>();

        String[] imageUrls = new String[]{
                "http://i2.cdn.cnn.com/cnnnext/dam/assets/160614121003-08-instant-vacation-restricted-super-169.jpg",
                "http://www.wonderslist.com/wp-content/uploads/2016/02/Warcraft-1.jpg"
        };

        String[] titles = new String[]{
            "This clean !","That haircut !"
        };

        String[] briefDescription = new String[]{
                "Everything that happens in the world of fashion happens for a reason.",
                "Distribution of resources among men can be a life changing aspect."
        };

        int count = imageUrls.length;

        for(int i = 0;i < count;i++){
            preDataList.add(new PreData(imageUrls[i],titles[i],briefDescription[i]));
        }

        return preDataList;
    }
}
