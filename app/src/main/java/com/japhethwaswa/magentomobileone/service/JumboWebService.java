package com.japhethwaswa.magentomobileone.service;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.db.DatabaseHelper;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;
import com.japhethwaswa.magentomobileone.event.CategoriesEventHandler;
import com.japhethwaswa.magentomobileone.model.Category;
import com.japhethwaswa.magentomobileone.model.Product;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static void serviceRetrieveCategories(final Context context) {

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
                            parseCategories(response, context, "0");
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

    //parse categories from xml
    private static void parseCategories(String response, Context context, String myParentId) throws IOException {

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

                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_CONTENT_TYPE)) {
                                category.setContent_type(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_PARENT_ID)) {
                                category.setParent_id(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_ICON)) {
                                category.setModification_time(myParser.getAttributeValue(null, JumboContract.CategoryEntry.COLUMN_MODIFICATION_TIME));
                                category.setIcon(myParser.nextText());
                            }
                            //set category my_parent_id passed to the method
                            category.setMy_parent_id(myParentId);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = myParser.getName();
                        if (name.equalsIgnoreCase("item") && category != null) {
                            //update,insert db
                            updateInsertCategories(context, category);

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

    }

    //db update,insert categories
    private static void updateInsertCategories(final Context context, final Category category) {

        Resources res = context.getResources();
        final String itemsCounted = res.getString(R.string.jumbo_product_count);
        final String itemsOffsets = "0";

        final ContentValues values = new ContentValues();
        values.put(JumboContract.CategoryEntry.COLUMN_LABEL, category.getLabel());
        values.put(JumboContract.CategoryEntry.COLUMN_ENTITY_ID, category.getEntity_id());
        values.put(JumboContract.CategoryEntry.COLUMN_CONTENT_TYPE, category.getContent_type());
        values.put(JumboContract.CategoryEntry.COLUMN_PARENT_ID, category.getParent_id());
        values.put(JumboContract.CategoryEntry.COLUMN_MY_PARENT_ID, category.getMy_parent_id());
        values.put(JumboContract.CategoryEntry.COLUMN_ICON, category.getIcon());
        values.put(JumboContract.CategoryEntry.COLUMN_MODIFICATION_TIME, category.getModification_time());

        String selection = JumboContract.CategoryEntry.COLUMN_ENTITY_ID + "=?";
        String[] selectionArgs = {category.getEntity_id()};

        JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver()) {
            @Override
            protected void onUpdateComplete(int token, Object cookie, int result) {
                //if no update then insert
                if (result == -1) {
                    this.startInsert(7, null, JumboContract.CategoryEntry.CONTENT_URI, values);
                }

                //comment below line if cpu consumption is high
                //get sub category dataa and products
                serviceSubCategoryData(context, category.getEntity_id(), category.getEntity_id(), itemsCounted, itemsOffsets);
            }
        };

        handler.startUpdate(5, null, JumboContract.CategoryEntry.CONTENT_URI, values, selection, selectionArgs);

    }

    //used if no category is not there
    public static void retrieveMainCategories(final Context context, final String itemsCount, final String itemsOffset) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;

        //since its first time just get configs from xml
        Resources res = context.getResources();
        String itemsCounted = res.getString(R.string.jumbo_product_count);
        String itemsOffsets = "0";

        String[] projection = {
                JumboContract.CategoryEntry.COLUMN_ENTITY_ID,
        };
        String selection = JumboContract.CategoryEntry.COLUMN_MY_PARENT_ID + "=?";
        String[] selectionArgs = {"0"};

        cursor = db.query(JumboContract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String entityId = cursor.getString(cursor.getColumnIndex(JumboContract.CategoryEntry.COLUMN_ENTITY_ID));
            serviceSubCategoryData(context, entityId, entityId, itemsCounted, itemsOffsets);
        }

        cursor.close();
        db.close();

    }


    //service retrieve sub-category products and mini-categories
    public static void serviceSubCategoryData(final Context context, String categoryId, final String parentCategory, final String itemsCount, final String itemsOffset) {
        //remember to save my_parent_id to category
        //remember to save both id's with the product ie product_ids(-4-,-2-)

        //fetch these categories/products
        Resources res = context.getResources();
        String relativeUrl = res.getString(R.string.jumbo_top_categories);
        relativeUrl = relativeUrl + "/id/" + categoryId + "/offset/" + itemsOffset + "/count/" + itemsCount;

        AndroidNetworking.get(getAbsoluteUrl(context, relativeUrl))
                .setTag("jumboCategoriesProducts")
                .setPriority(Priority.HIGH)
                .addHeaders("Cookie", getCookie(context))
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseCategoriesProducts(response, context, parentCategory);
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

    private static void parseCategoriesProducts(String response, Context context, String parentCategory) throws IOException {


        InputStream xmlStream = new ByteArrayInputStream(response.getBytes());
        Boolean isCat = true;
        Boolean isProd = false;
        Category category = null;
        Product product = null;

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
                            isProd = true;
                        }

                        //get categories
                        if (name.equalsIgnoreCase("item") && isCat == true) {
                            category = new Category();
                        } else if (category != null) {
                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_LABEL)) {
                                category.setLabel(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_ENTITY_ID)) {
                                category.setEntity_id(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_CONTENT_TYPE)) {
                                category.setContent_type(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_PARENT_ID)) {
                                category.setParent_id(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.CategoryEntry.COLUMN_ICON)) {
                                category.setModification_time(myParser.getAttributeValue(null, JumboContract.CategoryEntry.COLUMN_MODIFICATION_TIME));
                                category.setIcon(myParser.nextText());
                            }
                            //set category my_parent_id passed to the method
                            category.setMy_parent_id(parentCategory);
                        }

                        //get products
                        if (name.equalsIgnoreCase("item") && isProd == true) {
                            product = new Product();
                        } else if (product != null) {
                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_ENTITY_ID)) {
                                product.setEntity_id(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_NAME)) {
                                product.setName(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_ENTITY_TYPE)) {
                                product.setEntity_type(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_SHORT_DESCRIPTION)) {
                                product.setShort_description(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_DESCRIPTION)) {
                                product.setDescription(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_LINK)) {
                                product.setLink(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_IN_STOCK)) {
                                product.setIn_stock(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_IS_SALABLE)) {
                                product.setIs_salable(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_HAS_GALLERY)) {
                                product.setHas_gallery(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_HAS_OPTIONS)) {
                                product.setHas_options(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_RATING_SUMMARY)) {
                                product.setRating_summary(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_REVIEWS_COUNT)) {
                                product.setReviews_count(myParser.nextText());
                            }

                            if (name.equalsIgnoreCase("price")) {
                                product.setPrice_regular(myParser.getAttributeValue(null, "regular"));
                            }

                            if (name.equalsIgnoreCase("price")) {
                                product.setPrice_special(myParser.getAttributeValue(null, "special"));
                            }

                            if (name.equalsIgnoreCase(JumboContract.ProductEntry.COLUMN_ICON)) {
                                product.setModification_time(myParser.getAttributeValue(null, JumboContract.ProductEntry.COLUMN_MODIFICATION_TIME));
                                product.setIcon(myParser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = myParser.getName();

                        if (name.equalsIgnoreCase("item") && category != null) {
                            //update,insert category
                            updateInsertCategories(context, category);
                            //set null
                            category = null;
                        }
                        if (name.equalsIgnoreCase("item") && product != null) {
                            //update,insert category
                            updateInsertProduct(context, product, parentCategory);

                            //set null
                            product = null;
                        }
                        break;

                }

                eventType = myParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    private static void updateInsertProduct(Context context, Product product, String parentCategory) {

        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        Boolean isUpdate = false;
        String previousCategories = null;
        String allCategoryIds;

        //use product table contract
        //deal with the category id appropriately
        //ensure the cursor fetched determines whether its an insert or update

        String[] projection = {
                JumboContract.ProductEntry.COLUMN_CATEGORY_IDS,
        };
        String selection = JumboContract.ProductEntry.COLUMN_ENTITY_ID + "=?";
        String[] selectionArgs = {product.getEntity_id()};

        cursor = db.query(JumboContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.getCount() == 1) {
            isUpdate = true;
            while (cursor.moveToNext()) {
                previousCategories = cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_CATEGORY_IDS));
            }
        }
        cursor.close();
        db.close();


        //new category_ids field using the previous present values
        if (previousCategories != null) {
            List<String> categoryIdsArray = new ArrayList<>(Arrays.asList(previousCategories.split(",")));

            if (!categoryIdsArray.contains("-" + parentCategory + "-")) {
                //add if not available
                categoryIdsArray.add("-" + parentCategory + "-");
            }

            allCategoryIds = android.text.TextUtils.join(",", categoryIdsArray);
        } else {
            allCategoryIds = "-" + parentCategory + "-";
        }


        JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver());

        ContentValues valuesed = new ContentValues();
        valuesed.put(JumboContract.ProductEntry.COLUMN_ENTITY_ID, product.getEntity_id());
        valuesed.put(JumboContract.ProductEntry.COLUMN_NAME, product.getName());
        valuesed.put(JumboContract.ProductEntry.COLUMN_ENTITY_TYPE, product.getEntity_type());
        valuesed.put(JumboContract.ProductEntry.COLUMN_SHORT_DESCRIPTION, product.getShort_description());
        valuesed.put(JumboContract.ProductEntry.COLUMN_DESCRIPTION, product.getDescription());//marked
        valuesed.put(JumboContract.ProductEntry.COLUMN_LINK, product.getLink());
        valuesed.put(JumboContract.ProductEntry.COLUMN_IN_STOCK, product.getIn_stock());
        valuesed.put(JumboContract.ProductEntry.COLUMN_IS_SALABLE, product.getIs_salable());
        valuesed.put(JumboContract.ProductEntry.COLUMN_HAS_GALLERY, product.getHas_gallery());
        valuesed.put(JumboContract.ProductEntry.COLUMN_HAS_OPTIONS, product.getHas_options());
        valuesed.put(JumboContract.ProductEntry.COLUMN_RATING_SUMMARY, product.getRating_summary());
        valuesed.put(JumboContract.ProductEntry.COLUMN_REVIEWS_COUNT, product.getReviews_count());
        valuesed.put(JumboContract.ProductEntry.COLUMN_PRICE_REGULAR, product.getPrice_regular());
        valuesed.put(JumboContract.ProductEntry.COLUMN_PRICE_SPECIAL, product.getPrice_special());
        valuesed.put(JumboContract.ProductEntry.COLUMN_ICON, product.getIcon());
        valuesed.put(JumboContract.ProductEntry.COLUMN_MODIFICATION_TIME, product.getModification_time());
        valuesed.put(JumboContract.ProductEntry.COLUMN_CATEGORY_IDS, allCategoryIds);

        if (isUpdate == true) {
            //perfom update
            String selectioned = JumboContract.ProductEntry.COLUMN_ENTITY_ID + "=?";
            String[] selectionArgsed = {product.getEntity_id()};
            handler.startUpdate(87, null, JumboContract.ProductEntry.CONTENT_URI, valuesed, selectioned, selectionArgsed);

        }

        if (isUpdate == false) {
            //perfom insert
            handler.startInsert(86, null, JumboContract.ProductEntry.CONTENT_URI, valuesed);
        }

    }


    public static void serviceRetrieveProductGallery(final Context context, final String productEntityId) {

        //fetch these categories/products
        Resources res = context.getResources();
        String relativeUrl = res.getString(R.string.jumbo_product_gallery);
        relativeUrl = relativeUrl + "/" + productEntityId;

        AndroidNetworking.get(getAbsoluteUrl(context, relativeUrl))
                .setTag("jumboproductGallery")
                .setPriority(Priority.HIGH)
                .addHeaders("Cookie", getCookie(context))
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseProductGallery(response, context, productEntityId);
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

    private static void parseProductGallery(String response, Context context, String productEntityId) throws IOException {

        //delete all galleryimages for this product
        JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver());

        //delete all gallery images for this product id
        String selection = JumboContract.ProductImagesEntry.COLUMN_ENTITY_ID + "=?";
        String[] selectionArgs = {productEntityId};
        handler.startDelete(77,null,JumboContract.ProductImagesEntry.CONTENT_URI,selection,selectionArgs);


        InputStream xmlStream = new ByteArrayInputStream(response.getBytes());
        Product product = null;
        Boolean isImageObject = false;
        //remember to set the isImageObject/isSmallImage to false when retrieved data

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
                        if (name.equalsIgnoreCase("image")) {
                            //if image object
                            isImageObject = true;
                        }

                        //get products
                        if (name.equalsIgnoreCase("file") && isImageObject == true) {
                            if (product == null) {
                                product = new Product();
                            }

                        }
                        if (product != null) {

                            if (myParser.getAttributeValue(null, "type").equalsIgnoreCase("big")) {
                                product.setEntity_id(productEntityId);
                                product.setModification_time(myParser.getAttributeValue(null, JumboContract.ProductImagesEntry.COLUMN_MODIFICATION_TIME));
                                product.setImage_url_big(myParser.getAttributeValue(null, "url"));
                                product.setImage_id(myParser.getAttributeValue(null, "id"));
                            } else {
                                product.setImage_url_small(myParser.getAttributeValue(null, "url"));
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = myParser.getName();

                        if (name.equalsIgnoreCase("image") && product != null) {
                            //update,insert product images
                            insertProductGallery(context, product, productEntityId);
                            //set null
                            product = null;
                            isImageObject = false;
                        }
                        break;

                }

                eventType = myParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    //insert product gallery
    private static void insertProductGallery(Context context, Product product, String productEntityId) {

        Log.e("jeff-waswa","we are ready");
        JumboQueryHandler handler = new JumboQueryHandler(context.getContentResolver());

        ContentValues values = new ContentValues();
        values.put(JumboContract.ProductImagesEntry.COLUMN_ENTITY_ID, product.getEntity_id());
        values.put(JumboContract.ProductImagesEntry.COLUMN_IMAGE_URL_BIG, product.getImage_url_big());
        values.put(JumboContract.ProductImagesEntry.COLUMN_IMAGE_URL_SMALL, product.getImage_url_small());
        values.put(JumboContract.ProductImagesEntry.COLUMN_IMAGE_ID, product.getImage_id());
        values.put(JumboContract.ProductImagesEntry.COLUMN_MODIFICATION_TIME, product.getModification_time());

        //perfom insert
        handler.startInsert(75, null, JumboContract.ProductImagesEntry.CONTENT_URI, values);

    }


}
