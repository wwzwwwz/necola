package com.example.necola.utils.httpAPI.resource.netease;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.necola.entity.Music;
import com.example.necola.utils.httpAPI.ResourceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Recommend extends ResourceUtil {

    static public class Playlists{
        String url;
        JSONObject currentResults;
        public ArrayList<Music.Recommend> recommends;
        Boolean hasMore;

        public Playlists(){
            this.url = MUSIC_LIBRARY_HOST + "recommend/resource";
            recommends=new ArrayList<>();
        }
        public ArrayList<Music.Recommend> get(Context context)  {

            hasMore=false;
            Response response = GET(context,url);
            if (response==null){
                new AlertDialog.Builder(context).setTitle("Error").setMessage("Network exception").create().show();
                return new ArrayList<>();
            }
            if (response.code()<300) {
                currentResults =handle_response(response);
                parsing();
                return recommends;
            }
            else return new ArrayList<>();


        }
        void parsing() {
            JSONArray recommendArray= null;
            try {
                recommendArray = currentResults.getJSONArray("recommend");

            for(int i=0;i<recommendArray.length();i++){
                JSONObject object=recommendArray.getJSONObject(i);
                recommends.add(new Music.Recommend(
                        object.getLong("id"),
                        object.getString("name"),
                        object.getString("copywriter"),
                        object.getString("picUrl")));
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    static public class Songs{
        String url;
        JSONObject currentResults;
        public ArrayList<Music.Song> recommends;
        Boolean hasMore;

        public Songs(){
            this.url = MUSIC_LIBRARY_HOST + "recommend/songs";
            recommends=new ArrayList<>();
        }
        public Boolean get(Context context) throws JSONException {

            hasMore=false;
            Response response = GET(url);
            if (response==null){
                new AlertDialog.Builder(context).setTitle("Error").setMessage("Network exception").create().show();
                return false;
            }
            if (response.code()<300) {
                currentResults =handle_response(response);
                parsing();
                return true;
            }
            else return false;


        }
        void parsing() throws JSONException {
        }

    }
}
