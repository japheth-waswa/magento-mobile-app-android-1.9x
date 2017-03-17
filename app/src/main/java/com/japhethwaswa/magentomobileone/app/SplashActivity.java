package com.japhethwaswa.magentomobileone.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.japhethwaswa.magentomobileone.db.JumboContract.PagerEntry;

import com.japhethwaswa.magentomobileone.db.DatabaseHelper;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;
import com.japhethwaswa.magentomobileone.job.RetrieveCategories;
import com.japhethwaswa.magentomobileone.job.RetrieveMainData;
import com.japhethwaswa.magentomobileone.job.builder.MyJobsBuilder;

public class SplashActivity extends AppCompatActivity {
    private JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //StrictMode
        if (BuildConfig.DEBUG) {
            StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setVmPolicy(vmPolicy);
        }
        /**==============**/

        super.onCreate(savedInstanceState);

        //check db if pager items and main items have been set(access db not supposed to be here)
        //DatabaseHelper helper = new DatabaseHelper(this);
        //SQLiteDatabase db = helper.getReadableDatabase();
        //createPagerItems();
        //updatePager();
        //deletePager();
        //readPagerItems();
        //upgradeCreatePagerItems();


        /****====****/
        //check if pager already exists(was viewed)
        Boolean pagerObsolete = pagerStatus();

        //initiate job to get both pager and main items
        jobManager = new JobManager(MyJobsBuilder.getConfigBuilder(getApplicationContext()));
        //job retrieve main data
        jobManager.addJobInBackground(new RetrieveMainData());
        //TODO job should retrieve both top categories,subcategories and 100 products for each category
        //job retrieve categories
        jobManager.addJobInBackground(new RetrieveCategories());


        if (pagerObsolete == true) {
            //load HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        if (pagerObsolete == false) {
            //load MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private Boolean pagerStatus() {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                PagerEntry.COLUMN_TITLE,
                PagerEntry.COLUMN_BRIEF_DESCRIPTION,
                PagerEntry.COLUMN_IMAGE_URL_LOCAL,
                PagerEntry.COLUMN_IMAGE_URL_REMOTE,
                PagerEntry.COLUMN_UPDATED_AT
        };
        Cursor cursor = db.query(PagerEntry.TABLE_NAME, projection, null, null, null, null, null);
        int pagerCount = cursor.getCount();
        cursor.close();
        db.close();

        if (pagerCount > 0) {
            //is obsolete don't show the MainActivity.
            return true;
        } else {
            //is not obsolete show the MainActivity
            return false;
        }
    }

    /**me-no**
    private void createPagerItems() {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PagerEntry.COLUMN_TITLE, "wolla");
        values.put(PagerEntry.COLUMN_BRIEF_DESCRIPTION, "brief description comes here");
        values.put(PagerEntry.COLUMN_IMAGE_URL_LOCAL, "http://magento-29325-63476-210388.cloudwaysapps.com/media/jumbo_mobile/Penguins.jpg");
        values.put(PagerEntry.COLUMN_IMAGE_URL_REMOTE, "");
        values.put(PagerEntry.COLUMN_UPDATED_AT, "2017-03-06 10:43:37");

        long pagerId = db.insert(PagerEntry.TABLE_NAME, null, values);
        db.close();
    }

    private void readPagerItems() {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                PagerEntry.COLUMN_TITLE,
                PagerEntry.COLUMN_BRIEF_DESCRIPTION,
                PagerEntry.COLUMN_IMAGE_URL_LOCAL,
                PagerEntry.COLUMN_IMAGE_URL_REMOTE,
                PagerEntry.COLUMN_UPDATED_AT
        };

        /**String selection = PagerEntry._ID +"=?";
         String[] selectionArgs = {"1"};**/

        //Cursor cursor = db.query(PagerEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
    /**me-no**    Cursor cursor = db.query(PagerEntry.TABLE_NAME, projection, null, null, null, null, null);
        int i = cursor.getCount();

        /**while(cursor.moveToNext()){
         Log.d("Record identifier",String.valueOf(cursor.getColumnIndex(PagerEntry._ID)));
         }**/
    /**me-no**    Log.d("row count", String.valueOf(i));
        cursor.close();
    }

    private void updatePager() {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PagerEntry.COLUMN_TITLE, "update title");
        values.put(PagerEntry.COLUMN_BRIEF_DESCRIPTION, "update brief description comes here");
        values.put(PagerEntry.COLUMN_IMAGE_URL_LOCAL, "update http://magento-29325-63476-210388.cloudwaysapps.com/media/jumbo_mobile/Penguins.jpg");
        values.put(PagerEntry.COLUMN_IMAGE_URL_REMOTE, "update");
        values.put(PagerEntry.COLUMN_UPDATED_AT, "update 2017-03-06 10:43:37");

        String selection = PagerEntry._ID + "=?";
        String[] selectionArgs = {"1"};
        int numRowsUpdated = db.update(PagerEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private void deletePager() {
        /**DatabaseHelper helper = new DatabaseHelper(this);
         SQLiteDatabase db = helper.getWritableDatabase();**/

      /**me-no**  String selection = PagerEntry._ID + "=?";
        String[] selectionArgs = {"1"};
        //int numRowsDeleted = db.delete(PagerEntry.TABLE_NAME,selection,selectionArgs);
        JumboQueryHandler handler = new JumboQueryHandler(this.getContentResolver());
        handler.startDelete(1, null, PagerEntry.CONTENT_URI, selection, selectionArgs);
    }

    private void upgradeCreatePagerItems() {
        ContentValues values = new ContentValues();
        values.put(PagerEntry.COLUMN_TITLE, "wolladem");
        values.put(PagerEntry.COLUMN_BRIEF_DESCRIPTION, "dem brief description comes here");
        values.put(PagerEntry.COLUMN_IMAGE_URL_LOCAL, "dem http://magento-29325-63476-210388.cloudwaysapps.com/media/jumbo_mobile/Penguins.jpg");
        values.put(PagerEntry.COLUMN_IMAGE_URL_REMOTE, "");
        values.put(PagerEntry.COLUMN_UPDATED_AT, "dem 2017-03-06 10:43:37");

        JumboQueryHandler handler = new JumboQueryHandler(this.getContentResolver());
        handler.startInsert(1, null, PagerEntry.CONTENT_URI, values);
        //Uri uri = getContentResolver().insert(PagerEntry.CONTENT_URI,values);
        //Log.d("SplashActivity","inserted page " + uri);
    }**me-no**/


}
