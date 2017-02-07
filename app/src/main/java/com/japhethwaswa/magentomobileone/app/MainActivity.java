package com.japhethwaswa.magentomobileone.app;

import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.MainViewPagerAdapter;
import com.japhethwaswa.magentomobileone.model.PreData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //StrictMode
        if(BuildConfig.DEBUG){
            StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setVmPolicy(vmPolicy);
        }
        /**==============**/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<PreData> pagerItem = getPreData();

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this,pagerItem);

        viewPager.setAdapter(mainViewPagerAdapter);

    }

    //bg thread to fetch data from an endpoint.
    public List<PreData> getPreData(){

        ArrayList<PreData> preDataList = new ArrayList<>();

        int[] imageIds = new int[]{
                R.drawable.alladin_forever_young_3,R.drawable.alladin_zechino_1
        };

        String[] titles = new String[]{
            "This clean !","That haircut !"
        };

        String[] briefDescription = new String[]{
                "Everything that happens in the world of fashion happens for a reason.",
                "Distribution of resources among men can be a life changing aspect."
        };

        int count = imageIds.length;

        for(int i = 0;i < count;i++){
            preDataList.add(new PreData(imageIds[i],titles[i],briefDescription[i]));
        }

        return preDataList;
    }
}
