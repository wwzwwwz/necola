package com.example.necola.utils.httpAPI.resource;


import androidx.annotation.NonNull;

import com.example.necola.utils.httpAPI.ResourceUtil;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth extends ResourceUtil{


    static public Boolean register(@NonNull String username, @NonNull String password) {


        String address = USER_LIBRARY_HOST + "users";

        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Response response = ResourceUtil.POST(address, requestBody);
        int code = response.code();

        return code < 300;

    }

    static public boolean login(@NonNull String username, @NonNull String password) {


        String address = ResourceUtil.USER_LIBRARY_HOST + "auths";


        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Response response = ResourceUtil.POST(address, requestBody);
        int code = response.code();
        return code < 300;

    }


}
