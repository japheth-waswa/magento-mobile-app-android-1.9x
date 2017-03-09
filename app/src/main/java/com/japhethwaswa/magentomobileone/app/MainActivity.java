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
import com.japhethwaswa.magentomobileone.job.RetrieveProducts;
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

    //comment
    /**me-no**@Override
     protected void onStart() {
     super.onStart();
     //getPreData();
     if(preDataList.size() < 1){
     getPagerData();
     }

     }**/

    /**android job manager and networking
     private void getMagentoResource(String customers) {
     jobManager.addJobInBackground(new RetrieveProducts(customers));
     //new GetCustomersFromURLTask().execute();

     AndroidNetworking.get("https://www.alladin.co.ke/alpesa")
     .setTag("NetTest")
     .setPriority(Priority.HIGH)
     .build().getAsString(new StringRequestListener() {
    @Override public void onResponse(String response) {
    Log.e("NetTestResponse",response);
    }

    @Override public void onError(ANError anError) {
    Log.e("NetTestError",anError.toString());
    }
    });

     }

     **/

    //bg thread to fetch data from an endpoint.

    //bg thread to fetch data from an endpoint.
    //public  ArrayList<PreData> getPreData() {
    /** public  void getPreData() {

     preDataList = new ArrayList<>();

     String[] imageUrls = new String[]{
     "http://i2.cdn.cnn.com/cnnnext/dam/assets/160614121003-08-instant-vacation-restricted-super-169.jpg",
     "http://www.wonderslist.com/wp-content/uploads/2016/02/Warcraft-1.jpg"
     };

     String[] titles = new String[]{
     "This clean !", "That haircut !"
     };

     String[] briefDescription = new String[]{
     "Everything that happens in the world of fashion happens for a reason.",
     "Distribution of resources among men can be a life changing aspect."
     };

     int count = imageUrls.length;
     preDataList.clear();
     for (int i = 0; i < count; i++) {
     PreData preData = new PreData();
     preData.setImageUrl(imageUrls[i]);
     preData.setTitle(titles[i]);
     preData.setBriefDescription(briefDescription[i]);
     preDataList.add(preData);
     }

     mainViewPagerAdapter.updatePagerItems(preDataList);
     //mainViewPagerAdapter.notifyDataSetChanged();

     }**/

    //comment
    /**me-no** private void getPagerData() {

     /**==**/
    /**me-no**String[] projection = {
     JumboContract.PagerEntry.COLUMN_TITLE,
     JumboContract.PagerEntry.COLUMN_BRIEF_DESCRIPTION,
     JumboContract.PagerEntry.COLUMN_IMAGE_URL_LOCAL,
     JumboContract.PagerEntry.COLUMN_IMAGE_URL_REMOTE,
     JumboContract.PagerEntry.COLUMN_UPDATED_AT
     };
     JumboQueryHandler handler = new JumboQueryHandler(this.getContentResolver()) {
    @Override protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
    try {
    if (cursor != null) {
    preDataList.clear();
    while (cursor.moveToNext()) {
    String title = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_TITLE));
    String briefDescription = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_BRIEF_DESCRIPTION));
    String localUrl = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_IMAGE_URL_LOCAL));
    String remoteUrl = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_IMAGE_URL_REMOTE));
    String updated_at = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_UPDATED_AT));

    //Log.e("item-val", title);
    //Log.e("item-val", updated_at);

    String imageUrl = localUrl;
    if (localUrl == null) {
    imageUrl = remoteUrl;
    }
    PreData preData = new PreData();
    preData.setImageUrl(imageUrl);
    preData.setTitle(title);
    preData.setBriefDescription(briefDescription);
    preDataList.add(preData);
    }
    //update adapter and automaticaly notifiy dataset changed
    mainViewPagerAdapter.updatePagerItems(preDataList);
    }
    cursor.close();
    } finally {

    }
    }
    };
     handler.startQuery(1, null, JumboContract.PagerEntry.CONTENT_URI, projection, null, null, null);
     /**===**/
    //me-no//}

    //comment

    /**
     * me-no** @Override
     * protected void onSaveInstanceState(Bundle outState) {
     * super.onSaveInstanceState(outState);
     * //save the predatalist to avoid refetching from db and check onstart if size is bigger before refetching from db
     * outState.putSerializable("mainPagerArray",preDataList);
     * <p>
     * }
     **/


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
