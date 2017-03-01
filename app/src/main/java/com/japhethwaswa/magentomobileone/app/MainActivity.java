package com.japhethwaswa.magentomobileone.app;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.birbit.android.jobqueue.JobManager;
import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.adapter.MainViewPagerAdapter;
import com.japhethwaswa.magentomobileone.job.MyJobsBuilder;
import com.japhethwaswa.magentomobileone.databinding.ActivityMainBinding;
import com.japhethwaswa.magentomobileone.job.RetrieveProducts;
import com.japhethwaswa.magentomobileone.model.PreData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private JobManager jobManager;

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

        //job manager efficient in running background processes.
       /** jobManager = new JobManager(MyJobsBuilder.getConfigBuilder(getApplicationContext()));

        getMagentoResource("alpesa");**/

        List<PreData> pagerItem = getPreData();

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this,pagerItem);

        activityMainBinding.mainViewPager.setAdapter(mainViewPagerAdapter);


    }

    /**android job manager and networking**
    private void getMagentoResource(String customers) {
        jobManager.addJobInBackground(new RetrieveProducts(customers));
        //new GetCustomersFromURLTask().execute();

        AndroidNetworking.get("https://www.alladin.co.ke/alpesa")
                .setTag("NetTest")
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.e("NetTestResponse",response);
            }

            @Override
            public void onError(ANError anError) {
                Log.e("NetTestError",anError.toString());
            }
        });

    }

    **/

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


    public void skipClicked(View view) {

        Intent intent = new Intent(this,NavDrawerActivity.class);
         startActivity(intent);
         finish();

    }

}
