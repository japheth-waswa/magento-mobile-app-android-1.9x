package com.japhethwaswa.magentomobileone.service;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by web0002 on 2/11/2017.
 */

public class MagentoWebService {

    private static String BASE_URL = "https://www.alladin.co.ke/";

    public static String retrieveAlpesa(String relativeUrl){
        try{

            URL url = new URL(getAbsoluteUrl(relativeUrl));
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
        return null;
    }


    private static String getAbsoluteUrl(String relativeUrl){
        return  BASE_URL + relativeUrl;
    }
}
