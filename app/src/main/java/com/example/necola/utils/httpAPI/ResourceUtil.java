package com.example.necola.utils.httpAPI;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResourceUtil{



    static final public String USER_LIBRARY_HOST ="http://159.75.16.150:8000/";
    static final public String MUSIC_LIBRARY_HOST ="http://159.75.16.150:3000/";

    static public Response POST(String address, RequestBody requestBody){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();

        Response response = null;

        try {
            response= client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
    static public JSONObject handle_response(Response response){
        JSONObject json=null;
        String resStr = "";

        try {
            resStr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            json = new JSONObject(resStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (json!=null) {
            return json;
        }
        else
        {
            json=new JSONObject();
            try{
                json.put("result",1);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return json;
        }

    }

    static public  Response GET(String address){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .get()
                .build();

        Response response = null;

        try {
            response= client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }




}

/*

 */


