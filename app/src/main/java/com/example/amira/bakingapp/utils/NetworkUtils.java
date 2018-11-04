package com.example.amira.bakingapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

public class NetworkUtils {

    // Debugging
    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    public static URL buildBakingDataUrl(){
        Uri bakingDataUri = Uri.parse(BASE_URL).buildUpon()
                .build();

        URL url = null;
        try{
            url = new URL(bakingDataUri.toString());
        }catch (MalformedURLException e){
            Log.d(LOG_TAG , e.getStackTrace().toString());
        }

        return url;
    }

    public static String getBakingData(URL dataUrl) throws IOException{

        HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();

        try{
//            InputStream in = dataUrl.openStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(in , Charset.forName("UTF-8")));
//
//            StringBuilder sb = new StringBuilder();
//            int flag;
//            while((flag = br.read()) != -1){
//                sb.append((char) flag);
//            }
//            return sb.toString();

            InputStream in = con.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();
            if(hasNext){
               return scanner.next();
            }else{
                return null;
            }

        }finally {
            con.disconnect();
        }
    }

    public static Uri buildVideoUri(String str){
        Uri uri = Uri.parse(str).buildUpon().build();
        return uri;
    }
}
