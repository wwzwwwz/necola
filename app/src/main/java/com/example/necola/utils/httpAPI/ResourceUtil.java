package com.example.necola.utils.httpAPI;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;


import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResourceUtil{

    private static OkHttpClient neteaseHttpClient;
    private static OkHttpClient generalHttpClient;
    public static OkHttpClient getNeteaseClientInstance(Context context ) {
        if (neteaseHttpClient != null) {
            return neteaseHttpClient;
        } else {
            CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
            neteaseHttpClient=new OkHttpClient().newBuilder().cookieJar(cookieJar).build();
            return neteaseHttpClient;
        }
    }
    public static OkHttpClient getAppClientInstance() {
        if (generalHttpClient != null) {
            return neteaseHttpClient;
        } else {
            generalHttpClient=new OkHttpClient();
            return generalHttpClient;
        }
    }

    static final public String USER_LIBRARY_HOST ="http://159.75.16.150:8000/";
    static final public String MUSIC_LIBRARY_HOST ="http://159.75.16.150:3000/";

    static public Response POST(Context context, String address, RequestBody requestBody){
        Request request=new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();

        Response response = null;

        try {
            response= getNeteaseClientInstance(context).newCall(request).execute();
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
       // OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .get()
                .build();

        Response response = null;

        try {
            response= getAppClientInstance().newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    static public  Response GET(Context context,String address){
        // OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .get()
                .build();

        Response response = null;

        try {
            response= getNeteaseClientInstance(context).newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    static public  void asyncGET(Context context,Callback callback,String address){
        // OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .get()
                .build();
        getNeteaseClientInstance(context).newCall(request).enqueue(callback);



    }

    static public  void asyncPOST(Context context,Callback callback,String address, RequestBody requestBody){
        // OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        getNeteaseClientInstance(context).newCall(request).enqueue(callback);


    }




}

/*

 */


