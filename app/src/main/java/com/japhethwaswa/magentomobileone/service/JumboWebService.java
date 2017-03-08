package com.japhethwaswa.magentomobileone.service;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.japhethwaswa.magentomobileone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JumboWebService {


    public static void retrieveMainData(Context context){
        //get relative url for this service
        Resources res = context.getResources();
        String relativeUrl = res.getString(R.string.jumbo_main_data_url);

        AndroidNetworking.get(getAbsoluteUrl(context,relativeUrl))
                .setTag("MainDataRequest")
        .setPriority(Priority.HIGH)
        .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("MainDataRequest",response.toString());
                //parse the json object extracting pager data and main data
                try {
                    JSONArray pagerArray = response.getJSONArray("pager");
                    Log.e("MainDataPagerArray",pagerArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("MainDataRequestError",anError.toString());
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


    private static String getAbsoluteUrl(Context context,String relativeUrl){
        Resources res = context.getResources();
        String baseUrl = res.getString(R.string.apiBaseUrl);
        return  baseUrl + relativeUrl;
    }
}
