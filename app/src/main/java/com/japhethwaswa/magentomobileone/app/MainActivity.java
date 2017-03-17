package com.japhethwaswa.magentomobileone.app;


import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;
import com.japhethwaswa.magentomobileone.job.builder.MyJobsBuilder;
import com.japhethwaswa.magentomobileone.databinding.ActivityMainBinding;
import com.japhethwaswa.magentomobileone.model.PreData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private  boolean dataAbsent = true;
    //private JobManager jobManager;
    private ArrayList<PreData> preDataList;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private ActivityMainBinding activityMainBinding;
    private static final int URL_LOADER = 0;
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
        //setContentView(R.layout.activity_main);

        //inflate layout
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //job manager efficient in running background processes.
        /**jobManager = new JobManager(MyJobsBuilder.getConfigBuilder(getApplicationContext()));

         getMagentoResource("alpesa");**/

        //List<PreData> pagerItems = getPreData();
        //preDataList = (ArrayList<PreData>) getPreData();
        /**me-no** preDataList = new ArrayList<>();

         if(savedInstanceState != null){
         preDataList = (ArrayList<PreData>) savedInstanceState.getSerializable("mainPagerArray");
         }


         //mainViewPagerAdapter = new MainViewPagerAdapter(this,pagerItems);
         mainViewPagerAdapter = new MainViewPagerAdapter(preDataList);**/
        //start loader
        activityMainBinding.mainPageLoader.startProgress();

        //start countdown to 10seconds if no data then load home view
        startCountDown();

        getSupportLoaderManager().initLoader(URL_LOADER, null, this);
        //getLoaderManager().initLoader(URL_LOADER,null,this);
        mainViewPagerAdapter = new MainViewPagerAdapter(cursor);

        activityMainBinding.mainViewPager.setAdapter(mainViewPagerAdapter);


    }

    private void startCountDown() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (dataAbsent == true) {
                    //no data in the cursor therefore load the HomeActivity
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();

    }

    public void skipClicked(View view) {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                JumboContract.PagerEntry.COLUMN_TITLE,
                JumboContract.PagerEntry.COLUMN_BRIEF_DESCRIPTION,
                JumboContract.PagerEntry.COLUMN_IMAGE_URL_LOCAL,
                JumboContract.PagerEntry.COLUMN_IMAGE_URL_REMOTE,
                JumboContract.PagerEntry.COLUMN_UPDATED_AT
        };

        return new CursorLoader(this, JumboContract.PagerEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //ensure you disable the loader only if cursor is null
        if(data.getCount() > 0){
            dataAbsent = false;
            activityMainBinding.mainPageLoader.stopProgress();
        }

        mainViewPagerAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mainViewPagerAdapter.setCursor(null);
    }
}
