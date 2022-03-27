package com.example.necola.entity;

import static com.example.necola.utils.httpAPI.ResourceUtil.handle_response;

import android.accounts.Account;

import com.example.necola.utils.httpAPI.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Music {

    static public class NeteaseStatus {
        class Account{
            Long id;
            int type;
            int status;
            public Account(Long id,int type,int status){
                this.id=id ;
                this.type=type;
                this.status=status;
            }
        }
        class Profile{
            Long id;
            String nickname;
            String avatarUrl;
            String backgroundUrl;
            String signature;
            int city;
            Long lastLoginTime;

            public Profile(Long id,String nickname,String avatarUrl,
                           String backgroundUrl,String signature){
                this.id=id;
                this.nickname=nickname;
                this.avatarUrl=avatarUrl;
                this.backgroundUrl=backgroundUrl;
                this.signature=signature;
            }
            public Profile(Long id,String nickname,String avatarUrl,
                           String backgroundUrl,String signature,
                           Long lastLoginTime){
                this.id=id;
                this.nickname=nickname;
                this.avatarUrl=avatarUrl;
                this.backgroundUrl=backgroundUrl;
                this.signature=signature;
                this.lastLoginTime=lastLoginTime;
            }
        }
        Account account;
        Profile profile;
        public NeteaseStatus(Account account,Profile profile ){
            this.account=account;
            this.profile=profile;
        }
    }

    static public  class Song  implements Serializable {
        long id;
        String name;
        ArrayList<Artist> artists;
        Album album;
        long mvId;
        SongData songData;

        public String getAlbumImg(){
            String s=getAlbum().getPicUrl();
            if(s==null|| s.equals("")){
                String uri=ResourceUtil.MUSIC_LIBRARY_HOST+"song/detail?ids="+this.id;
                JSONObject result=ResourceUtil.handle_response(ResourceUtil.GET(uri));
                try {
                    JSONObject data= (JSONObject) result.getJSONArray("songs").get(0);
                    return data.getJSONObject("al").getString("picUrl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return s;
        }
        public Song(long id, String name, ArrayList<Artist> artists, Album album, long mvId){
            this.id=id;
            this.name=name;
            this.artists=artists;
            this.album=album;
            this.mvId=mvId;
            //songData=getSongData();
        }
        public String getArrayListToString(){
            String temp="";
            for( int i=0;i<this.artists.size();i++)
            {
                if (i!=this.artists.size()-1)
                    temp=temp+this.artists.get(i).getName()+"/";
                else
                    temp=temp+this.artists.get(i).getName();
            }

            return  temp;
        }
        public String getTitle(){
            return this.name;
        }
        public long getId(){
            return this.id;
        }
        public long getMvId(){
            return this.mvId;
        }
        public ArrayList getArtists(){
            return this.artists;
        }
        public Album getAlbum(){
            return this.album;
        }

        public Boolean check(){
            return true;
        }
        public void setSongData(){


            JSONObject data= null;
            try {
                String uri=ResourceUtil.MUSIC_LIBRARY_HOST+"song/url?id="+this.id;
                JSONObject result=ResourceUtil.handle_response(ResourceUtil.GET(uri));
                data = (JSONObject) result.getJSONArray("data").get(0);

                MusicType type;
                switch (data.getString("type")){
                    case "mp3": type=MusicType.mp3;break;
                    case "flac": type=MusicType.flac;break;
                    case "wav": type=MusicType.wav;break;
                    default:type=MusicType.other;

                }

                this.songData= new SongData(
                        data.getString("url"),
                        data.getString("md5"),
                        data.getInt("br"),
                        type
                        );

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public SongData getSongData() {
            if (null==this.songData)
                setSongData();
            return this.songData;
        }



        enum MusicType{
            mp3,flac,wav,other
        }
        public class SongData implements Serializable{
            String url;
            int br;
            int size;
            String md5;
            MusicType type;

            SongData(String url,String md5,int br,MusicType type){
                this.url=url;
                this.md5=md5;
                this.br=br;
                this.type=type;
            }

            public String getUrl() {
                return url;
            }
        }




    }
    static public class Album implements Serializable{
        long id;
        String name;
        Artist artist;
        String picUrl;
        ArrayList<Artist> artists;
        public String getPicUrl() {
            return picUrl;
        }

        public Album(long id, String name, String picUrl,Artist artist){
            this.id=id;
            this.name=name;
            this.picUrl=picUrl;
            this.artist=artist;

        }
        public Album(long id, String name, String picUrl,ArrayList<Artist> artists){
            this.id=id;
            this.name=name;
            this.picUrl=picUrl;
            this.artists=artists;

        }
        public Album(long id, String name, String picUrl){
            this.id=id;
            this.name=name;
            this.picUrl=picUrl;

        }

        public Artist getArtist() {
            return artist;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
    static public class Artist implements Serializable{
        long id;
        String name;
        String picUrl;
        public Artist(long id, String name,String picUrl){
            this.id=id;
            this.name=name;
            this.picUrl=picUrl;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    static public class Playlist{

        static class Classification{

        }

        long id;
        String name;
        String coverImgUrl;
        ArrayList<Song> songArrayList;
        NeteaseStatus.Profile creator;

        public Playlist(long id,String name,String coverImgUrl){
            this.id=id;
            this.name=name;
            this.coverImgUrl=coverImgUrl;

        }

        public Playlist(long id, String name, String coverImgUrl, NeteaseStatus.Profile creator){
            this.id=id;
            this.name=name;
            this.coverImgUrl=coverImgUrl;
            this.creator=creator;

        }


        public ArrayList<Song> getSongArrayList() {
            return songArrayList;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }
    }
    public static class Recommend {
        long id;
        String name;
        String copywriter;
        String picUrl;
        public Recommend(long id,String name,String copywriter,String picUrl){
            this.id=id;
            this.name=name;
            this.copywriter=copywriter;
            this.picUrl=picUrl;

        }
        public void setCopywriter(String copywriter) {
            this.copywriter = copywriter;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }

        public String getCopywriter() {
            return copywriter;
        }

        public String getPicUrl() {
            return picUrl;
        }
    }


    static public class PlayQueue{
        ArrayList<Song> playingSongsQueue;
        ArrayList<Song> localSongsQueue;
        ArrayList<Song> recentSongsQueue;
        ArrayList<Album> recentAlbums;
        ArrayList<Playlist> recentPlaylists;
        ArrayList<Recommend> recommendPlaylists;

        public PlayQueue(){}
    }

    static public class RecentPlay<T> implements Serializable{
        String id;
        long time;
        T resource;
        public RecentPlay(T res, String id, long time){
            this.resource=res;
            this.id=id;
            this.time=time;
        }

        public T getResource() {
            return resource;
        }

        public long getTime() {
            return time;
        }

        public String getId() {
            return id;
        }
    }


}
