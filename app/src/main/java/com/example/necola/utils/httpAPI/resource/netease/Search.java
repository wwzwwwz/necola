package com.example.necola.utils.httpAPI.resource.netease;

import android.app.AlertDialog;
import android.content.Context;

import com.example.necola.entity.Music;
import com.example.necola.utils.httpAPI.ResourceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

public class Search extends ResourceUtil {
    String url;
    JSONObject currentResults;
    public ArrayList<Music.Song> songs;
    Boolean hasMore;

    public Search(Builder builder){
        this.url=builder.url;
        songs=new ArrayList<>();
    }
    public Boolean doSearch(Context context){

        hasMore=false;
        Response response = ResourceUtil.GET(url);
        if (response==null){
            new AlertDialog.Builder(context).setTitle("Error").setMessage("Network exception").create().show();
            return false;
        }
        if (response.code()<300) {
            currentResults = ResourceUtil.handle_response(response);
            parsing();
            return true;
        }
        else return false;


    }
    Boolean hasMore(){
        return hasMore;
    }

    void parsing(){
        try {

            JSONObject result=currentResults.getJSONObject("result");
            JSONArray songsArray=result.getJSONArray("songs");
            for (int i=0;i<songsArray.length();i++)
            {

                // parsing artists array
                JSONArray artistsArray =songsArray.getJSONObject(i).getJSONArray("artists");
                ArrayList<Music.Artist> artists=new ArrayList<>();
                for (int j=0;j<artistsArray.length();j++){
                    Music.Artist artist=new Music.Artist(
                    artistsArray.getJSONObject(j).getInt("id"),
                            artistsArray.getJSONObject(j).getString("name"),
                            artistsArray.getJSONObject(j).getString("picUrl"));
                    artists.add(artist);
                }

                // parsing albums
                JSONObject albumObject=songsArray.getJSONObject(i).getJSONObject("album");
                JSONObject albumArtistObject=albumObject.getJSONObject("artist");
                Music.Artist albumArtist=new Music.Artist(
                        albumArtistObject.getInt("id"),
                        albumArtistObject.getString("name"),
                        albumArtistObject.getString("picUrl")
                        );
                Music.Album album=new Music.Album(albumObject.getInt("id"),
                        albumObject.getString("name"),
                        "",
                        //albumObject.getString("picUrl"),
                        albumArtist);


                Music.Song song=new Music.Song(songsArray.getJSONObject(i).getInt("id"),
                        songsArray.getJSONObject(i).getString("name"),
                        artists,
                        album,
                        songsArray.getJSONObject(i).getInt("mvid"));
                songs.add(song);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    static public class Builder{

        String url;

        public Builder(String keywords){
            this.url = MUSIC_LIBRARY_HOST + "search/?keywords=" + keywords;

        }


        public Builder setLimit(int limit){
            this.url = this.url+"&limit="+limit ;
            return this;
        }
        public Builder setOffset(int offset){

            this.url = this.url+"&offset=" +offset ;
            return this;
        }
        public Search build(){
            return new Search(this);
        }
    }







}
