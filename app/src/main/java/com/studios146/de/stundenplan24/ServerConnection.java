package com.studios146.de.stundenplan24;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Florian Mansfeld on 21.04.2017
 */
class ServerConnection extends AsyncTask<String,Void,JSONObject> {
    private Context c;

    ServerConnection(Context context){
        this.c = context;
    }

    private String downloadFromUrl(String school) throws IOException {
        String urlstring = this.c.getResources().getString(R.string.baseurl) + school;
        InputStream is = null;

        //String content = "";

        try{
            URL url = new URL(urlstring);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds*/);
            conn.setConnectTimeout(15000 /*milliseconds*/);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            //int response = conn.getResponseCode();
            //Log.d("146s", "response: " + response);
            is = conn.getInputStream();

            //Convert the InputStream into a String
            String contentAsString = convertStreamToString(is);
            Log.d("146s","content: "+contentAsString);
            //content=contentAsString;
            return contentAsString;
        }catch (IOException e){
            //Log.d("146s","content: "+content);
            Log.d("146s",e.getMessage());
        }finally {
            if(is != null){
                try{
                    is.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private static String convertStreamToString(InputStream is){
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    protected JSONObject doInBackground(String... school){
        try{
            return new JSONObject(downloadFromUrl(school[0]));
        }catch(IOException e){
            Log.d("146s","Load failed");
            return null;
        }catch(JSONException e){
            Log.d("146s","Parsing failed");
            return null;
        }
    }
}
