package com.japhethwaswa.magentomobileone.service;

import android.content.ContentProvider;
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
import com.japhethwaswa.magentomobileone.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JumboWebService {


    //service-one request that retrieves both pager and maindata items
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
                // Log.e("MainDataRequest", response.toString());
                //parse the json object extracting pager data and main data
                try {
                    JSONArray pagerArray = response.getJSONArray("pager");
                    // pagerArray.length();
                    if (pagerArray.length() > 0) {
                        insertPager(pagerArray, context);
                    }


                    JSONArray mainArray = response.getJSONArray("main");
                    // pagerArray.length();
                    if (mainArray.length() > 0) {
                        updateMain(mainArray, context);
                    }
                    /**Log.e("MainDataPagerArray", pagerArray.toString());
                     Log.e("MainDataPagerArrLength", String.valueOf(pagerArray.length()));**/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                //Log.e("MainDataRequestError", anError.toString());
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

    //update the home items
    private static void updateMain(JSONArray mainArray, Context context) {

        //foreach item update
        for (int i = 0; i <= mainArray.length(); i++) {
            try {

                JSONObject mainObject = mainArray.getJSONObject(i);
                final ContentValues values = new ContentValues();
                String keyHome = mainObject.getString("key_home");
                values.put(JumboContract.MainEntry.COLUMN_KEY_HOME, keyHome);
                values.put(JumboContract.MainEntry.COLUMN_CATEGORY_ID, mainObject.getString("category_id"));
                values.put(JumboContract.MainEntry.COLUMN_PRODUCT_ID, mainObject.getString("product_id"));
                values.put(JumboContract.MainEntry.COLUMN_IMAGE_URL, mainObject.getString("image_url"));
                values.put(JumboContract.MainEntry.COLUMN_SECTION, mainObject.getString("section"));
                String title = null;
                if (mainObject.has("categoryTitle")) {
                    title = mainObject.getString("categoryTitle");
                }
                values.put(JumboContract.MainEntry.COLUMN_TITLE, title);
                values.put(JumboContract.MainEntry.COLUMN_UPDATED_AT, mainObject.getString("updated_at"));

                String selection = JumboContract.MainEntry.COLUMN_KEY_HOME + "=?";
                String[] selectionArgs = {keyHome};

                JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver()) {
                    @Override
                    protected void onUpdateComplete(int token, Object cookie, int result) {

                        //no data therefore insert
                        if (result == -1) {
                            this.startInsert(5, null, JumboContract.MainEntry.CONTENT_URI, values);
                        }

                    }
                };
                handler.startUpdate(3, null, JumboContract.MainEntry.CONTENT_URI, values, selection, selectionArgs);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //insert to db pager items
    private static void insertPager(JSONArray pagerArray, Context context) {

        JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver());
        //delete all previous data
        handler.startDelete(2, null, JumboContract.PagerEntry.CONTENT_URI, null, null);

        //perform new inserts
        for (int i = 0; i <= pagerArray.length(); i++) {
            try {

                JSONObject pagerObject = pagerArray.getJSONObject(i);

                //Log.e("item-1", pagerObject.getString("title"));

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

    //retrive cookie for accessing remote resource
    private static String getCookie(Context context) {
        Resources res = context.getResources();
        String appCode = res.getString(R.string.app_xmlconnect_code);
        String appDefaultScreenSize = res.getString(R.string.app_xmlconnect_screen_size);
        return "app_code=" + appCode + ";screen_size=" + appDefaultScreenSize;
    }

    //service-retrive all categories
    public static void retrieveCategories(final Context context) {

        Resources res = context.getResources();
        String relativeUrl = res.getString(R.string.jumbo_top_categories);

        AndroidNetworking.get(getAbsoluteUrl(context, relativeUrl))
                .setTag("jumboCategories")
                .setPriority(Priority.HIGH)
                .addHeaders("Cookie", getCookie(context))
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseCategoriesUpdate(response, context, "0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("jeff-error", anError.toString());
                    }
                });
    }

    //updaete categories table
    private static void parseCategoriesUpdate(String response, Context context, String myParentId) throws IOException {

        InputStream xmlStream = new ByteArrayInputStream(response.getBytes());
        Boolean isCat = true;
        Category category = null;

        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlPullParserFactory.newPullParser();
            myParser.setInput(xmlStream, null);

            int eventType = myParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String name;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        name = myParser.getName();
                        if (name.equalsIgnoreCase("products")) {
                            //if products have been reached then set the val to false ie not category now
                            isCat = false;
                        }

                        if (name.equalsIgnoreCase("item") && isCat == true) {
                            category = new Category();
                        } else if (category != null) {
                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_LABEL)) {
                                category.setLabel(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_ENTITY_ID)) {
                                category.setEntity_id(myParser.nextText());
                            }
                            //todo get the remainig data
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = myParser.getName();
                        if (name.equalsIgnoreCase("item") && category != null) {
                            //todo save to db in another method by passing the category object
                            Log.e("jeff-label", category.getLabel());
                            Log.e("jeff-entity_id", category.getEntity_id());

                            //set null
                            category = null;
                        }
                        break;

                }

                eventType = myParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        /** final ContentValues values = new ContentValues();
         values.put("","");

         String selection = JumboContract.CategoryEntry.COLUMN_ENTITY_ID + "=?";
         String selectionArgs[] = {"1"};

         JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver()){
        @Override protected void onUpdateComplete(int token, Object cookie, int result) {
        //if no update then insert
        if(result == -1){
        this.startInsert(98,null,JumboContract.CategoryEntry.CONTENT_URI,values);
        }
        }
        };

         handler.startUpdate(99,null,JumboContract.CategoryEntry.CONTENT_URI,values,selection,selectionArgs);**/

    }
}
