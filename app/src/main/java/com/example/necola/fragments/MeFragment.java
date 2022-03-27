package com.example.necola.fragments;

import static com.example.necola.utils.httpAPI.ResourceUtil.handle_response;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import androidx.viewpager2.widget.ViewPager2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.necola.AggregationActivity;
import com.example.necola.R;
import com.example.necola.entity.Music;
import com.example.necola.fragments.collections.CollectedAlbumFragment;
import com.example.necola.fragments.collections.ArtistFragment;
import com.example.necola.fragments.collections.CollectionFragment;
import com.example.necola.fragments.collections.OriginalPlaylistFragment;
import com.example.necola.fragments.collections.PagerCollectionAdapter;
import com.example.necola.fragments.collections.CollectedPlaylistsFragment;
import com.example.necola.fragments.dialog.RecentAlbumListDialogFragment;
import com.example.necola.fragments.dialog.RecentPlaySongListDialogFragment;
import com.example.necola.fragments.dialog.RecentPlaylistDialogFragment;
import com.example.necola.utils.httpAPI.ResourceUtil;
import com.example.necola.utils.httpAPI.resource.netease.NeteaseMusicAPI;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MeFragment extends Fragment {

    final String ROUTE_RECENT_SONG="record/recent/song";
    final String ROUTE_RECENT_PLAYLIST="record/recent/playlist";
    final String ROUTE_RECENT_ALBUM="record/recent/album";
    ViewPager2 pager;
    PagerCollectionAdapter collectionAdapter;

    JSONObject currentResults;

    List<CollectionFragment> fragmentList;
    //TabLayout tabLayout

    RecentPlaySongListDialogFragment songDialogFragment;
    RecentAlbumListDialogFragment albumDialogFragment;
    RecentPlaylistDialogFragment playlistDialogFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        collectionAdapter=new PagerCollectionAdapter(this);
        initFragmentList();
        collectionAdapter.setFragmentList(fragmentList);


        pager=view.findViewById(R.id.pager);
        pager.setAdapter(collectionAdapter);
        pager.setSaveEnabled(false);// To avoid error: Fragment no longer exists for key f#0


        return view;
    }
    void initFragmentList(){


        fragmentList=new ArrayList<>();
        fragmentList.add(OriginalPlaylistFragment.newInstance(R.string.original_playlist));
        fragmentList.add(CollectedPlaylistsFragment.newInstance(R.string.collected_playlist));
        fragmentList.add(CollectedAlbumFragment.newInstance(R.string.collected_album));
        fragmentList.add(ArtistFragment.newInstance(R.string.artist));
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setText(getString(fragmentList.get(position).getArguments().getInt("title")))
        ).attach();

        Button sync=view.findViewById(R.id.sync_third_party);
        sync.setOnClickListener(listener-> startActivity(new Intent(getActivity(), AggregationActivity.class)));

        MaterialCardView recentSong=view.findViewById(R.id.recent_song);

        recentSong.setOnClickListener(listener -> {

            songDialogFragment=RecentPlaySongListDialogFragment.newInstance();
            songDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog");
            Callback callback=new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() < 300) {
                        handleRecentSong(response);
                    }
                }
            };
            NeteaseMusicAPI.ResourceUrl resUrl=new NeteaseMusicAPI.ResourceUrl.Builder(ROUTE_RECENT_SONG)
                    .setLimit(30)
                    .build();
            ResourceUtil.asyncGET(getContext(),callback,resUrl.getUrl());

        });

        MaterialCardView recentPlaylist=view.findViewById(R.id.recent_playlist);
        recentPlaylist.setOnClickListener(listener ->
        {
            playlistDialogFragment=RecentPlaylistDialogFragment.newInstance();
            playlistDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog");
            Callback callback=new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() < 300) {
                        handleRecentPlaylist(response);
                    }
                }
            };
            NeteaseMusicAPI.ResourceUrl resUrl=new NeteaseMusicAPI.ResourceUrl.Builder(ROUTE_RECENT_PLAYLIST)
                    .setLimit(30)
                    .build();
            ResourceUtil.asyncGET(getContext(),callback,resUrl.getUrl());
        });

        MaterialCardView recentAlbum=view.findViewById(R.id.recent_album);
        recentAlbum.setOnClickListener(listener ->
        {
            albumDialogFragment=RecentAlbumListDialogFragment.newInstance();
            albumDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog");
            Callback callback=new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() < 300) {
                        handleRecentAlbum(response);
                    }
                }
            };

            NeteaseMusicAPI.ResourceUrl resUrl=new NeteaseMusicAPI.ResourceUrl.Builder(ROUTE_RECENT_ALBUM)
                    .setLimit(30)
                    .build();


            ResourceUtil.asyncGET(getContext(),callback,resUrl.getUrl());
         });
    }

    void handleRecentSong(Response response){
        currentResults = handle_response(response);
        ArrayList<Music.RecentPlay<Music.Song>> recentPlayArrayList=new ArrayList<>();
        try {
            JSONObject data=currentResults.getJSONObject("data");
            JSONArray dataArray = data.getJSONArray("list");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                JSONObject objectData=object.getJSONObject("data");

                // parsing artists array
                JSONArray artistsArray =objectData.getJSONArray("ar");
                ArrayList<Music.Artist> artists=new ArrayList<>();
                for (int j=0;j<artistsArray.length();j++){
                    Music.Artist artist=new Music.Artist(
                            artistsArray.getJSONObject(j).getInt("id"),
                            artistsArray.getJSONObject(j).getString("name"),
                            "");
                    artists.add(artist);
                }

                // parsing albums
                JSONObject albumObject=objectData.getJSONObject("al");
                Music.Album album=new Music.Album(
                        albumObject.getInt("id"),
                        albumObject.getString("name"),
                        albumObject.getString("picUrl")
                        );


                Music.Song song=new Music.Song(objectData.getInt("id"),
                        objectData.getString("name"),
                        artists,
                        album,
                        0);

                Music.RecentPlay<Music.Song> recentPlay=new Music.RecentPlay<>(
                        song,
                        object.getString("resourceId"),
                        object.getLong("playTime")
                );
                recentPlayArrayList.add(recentPlay);

            }
            if (getActivity()!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        songDialogFragment.refresh(recentPlayArrayList);

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void handleRecentPlaylist(Response response){
        currentResults = handle_response(response);
        ArrayList<Music.RecentPlay<Music.Playlist>> recentPlayArrayList=new ArrayList<>();
        try {
            JSONObject data=currentResults.getJSONObject("data");
            JSONArray dataArray = data.getJSONArray("list");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                JSONObject objectData=object.getJSONObject("data");

                Music.Playlist playlist=new Music.Playlist(objectData.getLong("id"),
                        objectData.getString("name"),
                        objectData.getString("coverImgUrl"));

                Music.RecentPlay<Music.Playlist> recentPlay=new Music.RecentPlay<>(
                        playlist,
                        object.getString("resourceId"),
                        object.getLong("playTime")
                );
                recentPlayArrayList.add(recentPlay);

            }
            if (getActivity()!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       playlistDialogFragment.refresh(recentPlayArrayList);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void handleRecentAlbum(Response response){
        currentResults = handle_response(response);
        ArrayList<Music.RecentPlay<Music.Album>> recentAlbumArrayList=new ArrayList<>();
        try {
            JSONObject data=currentResults.getJSONObject("data");
            JSONArray dataArray = data.getJSONArray("list");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                JSONObject objectData=object.getJSONObject("data");

                // parsing artists array
                JSONArray artistsArray =objectData.getJSONArray("artists");
                ArrayList<Music.Artist> artists=new ArrayList<>();
                for (int j=0;j<artistsArray.length();j++){
                    Music.Artist artist=new Music.Artist(
                            artistsArray.getJSONObject(j).getInt("id"),
                            artistsArray.getJSONObject(j).getString("name"),
                            "");
                    artists.add(artist);
                }


                // parsing song
                JSONArray songArray=objectData.getJSONArray("songs");
                ArrayList<Music.Song> songArrayList=new ArrayList<>();
                for( int i1=0;i1<songArray.length();i1++)
                {
                    JSONObject songObject=songArray.getJSONObject(i1);
                    Music.Song song=new Music.Song(
                            songObject.getLong("id"),
                            songObject.getString("name"),
                            artists,
                            null,
                            songObject.getLong("mvid")
                    );
                    songArrayList.add(song);

                }
                Music.Album album=new Music.Album(objectData.getInt("id"),
                        objectData.getString("name"),
                        objectData.getString("picUrl"),
                        artists);


                Music.RecentPlay<Music.Album> recentPlay=new Music.RecentPlay<>(
                        album,
                        object.getString("resourceId"),
                        object.getLong("playTime")
                );
                recentAlbumArrayList.add(recentPlay);

            }
            if (getActivity()!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        albumDialogFragment.refresh(recentAlbumArrayList);
                        // RecentPlaySongListDialogFragment.newInstance(recentPlayArrayList).show(getActivity().getSupportFragmentManager(), "dialog");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


   void  handleUserPlaylist(Response response){
       currentResults = handle_response(response);
       ArrayList<Music.RecentPlay<Music.Playlist>> recentPlayArrayList=new ArrayList<>();
       try {
           JSONObject data=currentResults.getJSONObject("data");
           JSONArray dataArray = data.getJSONArray("list");

           for (int i = 0; i < dataArray.length(); i++) {
               JSONObject object = dataArray.getJSONObject(i);
               JSONObject objectData=object.getJSONObject("data");

               Music.Playlist playlist=new Music.Playlist(objectData.getLong("id"),
                       objectData.getString("name"),
                       objectData.getString("coverImgUrl"));

               Music.RecentPlay<Music.Playlist> recentPlay=new Music.RecentPlay<>(
                       playlist,
                       object.getString("resourceId"),
                       object.getLong("playTime")
               );
               recentPlayArrayList.add(recentPlay);

           }
           if (getActivity()!=null){
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       playlistDialogFragment.refresh(recentPlayArrayList);
                   }
               });
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
    }


    void handleUserAlbum(Response response){
        currentResults = handle_response(response);
        ArrayList<Music.RecentPlay<Music.Album>> recentAlbumArrayList=new ArrayList<>();
        try {
            JSONObject data=currentResults.getJSONObject("data");
            JSONArray dataArray = data.getJSONArray("list");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                JSONObject objectData=object.getJSONObject("data");

                // parsing artists array
                JSONArray artistsArray =objectData.getJSONArray("artists");
                ArrayList<Music.Artist> artists=new ArrayList<>();
                for (int j=0;j<artistsArray.length();j++){
                    Music.Artist artist=new Music.Artist(
                            artistsArray.getJSONObject(j).getInt("id"),
                            artistsArray.getJSONObject(j).getString("name"),
                            "");
                    artists.add(artist);
                }


                // parsing song
                JSONArray songArray=objectData.getJSONArray("songs");
                ArrayList<Music.Song> songArrayList=new ArrayList<>();
                for( int i1=0;i1<songArray.length();i1++)
                {
                    JSONObject songObject=songArray.getJSONObject(i1);
                    Music.Song song=new Music.Song(
                            songObject.getLong("id"),
                            songObject.getString("name"),
                            artists,
                            null,
                            songObject.getLong("mvid")
                    );
                    songArrayList.add(song);

                }
                Music.Album album=new Music.Album(objectData.getInt("id"),
                        objectData.getString("name"),
                        objectData.getString("picUrl"),
                        artists);


                Music.RecentPlay<Music.Album> recentPlay=new Music.RecentPlay<>(
                        album,
                        object.getString("resourceId"),
                        object.getLong("playTime")
                );
                recentAlbumArrayList.add(recentPlay);

            }
            if (getActivity()!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        albumDialogFragment.refresh(recentAlbumArrayList);
                        // RecentPlaySongListDialogFragment.newInstance(recentPlayArrayList).show(getActivity().getSupportFragmentManager(), "dialog");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







}

