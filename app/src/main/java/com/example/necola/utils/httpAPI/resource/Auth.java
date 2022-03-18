package com.example.necola.utils.httpAPI.resource;


import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.necola.LoggedOutActivity;
import com.example.necola.utils.httpAPI.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth extends ResourceUtil{


    static public Boolean register(Context context,@NonNull String username, @NonNull String password) {


        String address = USER_LIBRARY_HOST + "users";

        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Response response = ResourceUtil.POST(context,address, requestBody);
        int code = response.code();

        return code < 300;

    }

    static public void login(Context context, Callback callback, @NonNull String username, @NonNull String password) {


        String address = ResourceUtil.USER_LIBRARY_HOST + "auths";


        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        ResourceUtil.asyncPOST(context, callback, address, requestBody);

        /*
        if (response==null){
            new AlertDialog.Builder(context).setTitle("Error").setMessage("Network exception").create().show();
            return false;
        }
        int code = response.code();
        if(code<300)return true;
        else{
            Toast.makeText(context, "account or password is invalid", Toast.LENGTH_SHORT).show();
            return false;
        }

         */

    }




}
