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
    static public class ResourceUrl{

        String url;
        public ResourceUrl(Builder builder){
            this.url=builder.url;
        }

        public String getUrl() {
            return url;
        }
        static public class Builder{
            Boolean noSet;
            String url;
            public Builder(String route){
                this.url= ResourceUtil.MUSIC_LIBRARY_HOST+route;
                this.noSet=true;
            }

            public Builder setType(int type){
                if (noSet)
                    this.url= this.url+"?type="+type;
                else
                    this.url= this.url+"&type="+type;
                noSet=false;
                return this;
            }

            public Builder setArea(int area){

                if (noSet)
                {
                    this.url= this.url+"?area="+area;
                    noSet=false;
                }
                else
                    this.url= this.url+"&area="+area;

                return this;
            }

            public Builder setLimit(int limit){
                if (noSet)
                {
                    this.url= this.url+"?limit="+limit;
                    noSet=false;
                }
                else
                    this.url= this.url+"&limit="+limit;

                return this;
            }
            public Builder setOffset(int offset){

                if (noSet) {
                    this.url = this.url + "?offset=";
                    noSet = false;
                }
                else
                    this.url= this.url+"&offset=";

                return this;
            }
            public Builder setInitial(String c){

                if (noSet)
                {
                    this.url= this.url+"?initial="+c;
                    noSet=false;
                }
                else
                    this.url= this.url+"&initial="+c;

                return this;
            }
            public ResourceUrl build(){
                noSet=true;
                return new ResourceUrl(this);
            }
        }



    }

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

    static public boolean syncLogout(Context context) {


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
