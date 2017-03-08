package com.japhethwaswa.magentomobileone.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JumboWebService {


    public static void retrieveMainData(final Context context) {
        //get relative url for this service
        Resources res = context.getResources();
        String relativeUrl = res.getString(R.string.jumbo_main_data_url);

        AndroidNetworking.get(getAbsoluteUrl(context, relativeUrl))
                .setTag("MainDataRequest")
                .setPriority(Priority.HIGH)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("MainDataRequest", response.toString());
                //parse the json object extracting pager data and main data
                try {
                    JSONArray pagerArray = response.getJSONArray("pager");
                    // pagerArray.length();
                    if (pagerArray.length() > 0) {
                        updatePager(pagerArray, context);
                    }
                    Log.e("MainDataPagerArray", pagerArray.toString());
                    Log.e("MainDataPagerArrLength", String.valueOf(pagerArray.length()));
                    
                    /**==**/
                    String[] projection = {
                            JumboContract.PagerEntry.COLUMN_TITLE,
                            JumboContract.PagerEntry.COLUMN_BRIEF_DESCRIPTION,
                            JumboContract.PagerEntry.COLUMN_IMAGE_URL_LOCAL,
                            JumboContract.PagerEntry.COLUMN_IMAGE_URL_REMOTE,
                            JumboContract.PagerEntry.COLUMN_UPDATED_AT
                    };
                    JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver()){
                        @Override
                        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                            try{
                                if(cursor != null){
                                    while(cursor.moveToNext()){
                                        String title = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_TITLE));
                                        String updated_at = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_UPDATED_AT));
                                        Log.e("item-val",title);
                                        Log.e("item-val",updated_at);
                                    }
                                }
                            }finally{

                            }
                        }
                    };
                    handler.startQuery(1, null, JumboContract.PagerEntry.CONTENT_URI, projection, null, null, null);
                    /**===**/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("MainDataRequestError", anError.toString());
            }
        });


        /**try{


         URL url = new URL(getAbsoluteUrl(context,relativeUrl));
         HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
         try{
         //process the request.
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
         StringBuilder stringBuilder = new StringBuilder();
         String line;
         while((line = bufferedReader.readLine()) != null){
         stringBuilder.append(line).append("\n");
         }
         //close the bufferedReader
         bufferedReader.close();
         Log.e("Response",stringBuilder.toString());
         return stringBuilder.toString();

         }finally {

         //disconnect
         urlConnection.disconnect();
         }

         }catch (Exception e){
         Log.e("ERROR-CONN",e.getMessage(),e);

         }
         return null;**/
    }

    private static void updatePager(JSONArray pagerArray, Context context) {

        JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver());
        //delete all previous data
        handler.startDelete(2,null,JumboContract.PagerEntry.CONTENT_URI,null,null);

        //perform new inserts
        for (int i = 0; i <= pagerArray.length(); i++) {
            try {

                JSONObject pagerObject = pagerArray.getJSONObject(i);
                Log.e("item-1", pagerObject.getString("title"));

                ContentValues values = new ContentValues();
                values.put(JumboContract.PagerEntry.COLUMN_TITLE, pagerObject.getString("title"));
                values.put(JumboContract.PagerEntry.COLUMN_BRIEF_DESCRIPTION, pagerObject.getString("brief_description"));
                values.put(JumboContract.PagerEntry.COLUMN_IMAGE_URL_LOCAL, pagerObject.getString("image_url_local"));
                values.put(JumboContract.PagerEntry.COLUMN_IMAGE_URL_REMOTE, pagerObject.getString("image_url_remote"));
                values.put(JumboContract.PagerEntry.COLUMN_UPDATED_AT, pagerObject.getString("updated_at"));

                //perform new insert
                handler.startInsert(1, null, JumboContract.PagerEntry.CONTENT_URI, values);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private static String getAbsoluteUrl(Context context, String relativeUrl) {
        Resources res = context.getResources();
        String baseUrl = res.getString(R.string.apiBaseUrl);
        return baseUrl + relativeUrl;
    }
}
