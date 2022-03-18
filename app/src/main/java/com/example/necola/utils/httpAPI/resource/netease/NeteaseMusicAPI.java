package com.example.necola.utils.httpAPI.resource.netease;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.necola.utils.httpAPI.ResourceUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class NeteaseMusicAPI extends ResourceUtil {

    static public boolean syncByLoginNetease(Context context, String username, @NonNull String password) {


        String address = ResourceUtil.MUSIC_LIBRARY_HOST + "login/cellphone?phone="+username+"&password="+password;

        Response response = ResourceUtil.GET(context,address);
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

    }

    static public boolean logout(Context context) {


        String address = ResourceUtil.MUSIC_LIBRARY_HOST + "logout";
        Response response = ResourceUtil.GET(context,address);
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

    }




}
