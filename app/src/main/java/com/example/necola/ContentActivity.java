package com.example.necola;

import static com.example.necola.utils.httpAPI.ResourceUtil.handle_response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.entity.Music;
import com.example.necola.fragments.DiscoverFragment;
import com.example.necola.fragments.SearchContentFragment;
import com.example.necola.fragments.collections.ArtistBoardFragment;
import com.example.necola.fragments.collections.CollectionFragment;
import com.example.necola.fragments.collections.SongBoardFragment;
import com.example.necola.fragments.collections.SyncNeteaseMusicFragment;
import com.example.necola.fragments.collections.SyncQQMusicFragment;
import com.example.necola.fragments.collections.TabViewpager2Fragment;
import com.example.necola.utils.httpAPI.ResourceUtil;
import com.example.necola.utils.httpAPI.resource.netease.ArtistFilter;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ContentActivity extends BaseActivity {

    private int currentMenuID;
    private Menu currentMenu;
    public  static void actionStart(Context context, Music.Song song,int layout){
        Intent intent=new Intent(context, ContentActivity.class);
        intent.putExtra("song_data",song);
        intent.putExtra("container_layout",layout);

        //overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        context.startActivity(intent);


    }

    //action bar item
    public boolean onOptionsItemSelected(MenuItem item) {
        //code for item select event handling
        switch (item.getItemId()) {
            //android.R 系统内部预先定义好的资源
            case android.R.id.home:
                onBackPressed();
                return true;
            //R 工程师自定义的资源
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(currentMenuID, menu);
        currentMenu=menu;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO lazy-load or pre-load.....
        loadContentById(getIntent().getIntExtra("container_layout",R.layout.activity_surface));
    }

    int getCodeByResID(int id){
        switch (id){
            case R.id.area_all:case R.id.artist_all:return -1;
            case R.id.area_chinese:return 7;
            case R.id.area_eu:return 96;
            case R.id.area_jp:return 8;
            case R.id.area_ko:return 16;
            case R.id.area_ot:return 0;
            case R.id.artist_male:return 1;
            case R.id.artist_female:return 2;
            case R.id.artist_group:return 3;

        };

        return id;
    };
    void loadContentById(int id){
        setContentView(id);
        switch (id){
            case R.layout.activity_content_search:

                Music.Song song=(Music.Song) getIntent().getSerializableExtra("song_data");

                SearchContentFragment searchContentFragment =(SearchContentFragment)
                        getSupportFragmentManager().findFragmentById(R.id.music_content_fragment);

                searchContentFragment.refresh(song);//刷新 相应Fragment界面
                break;

                //TODO
            case R.layout.activity_content_leaderboard:

                ArrayList<CollectionFragment> fragmentArrayList=new ArrayList<>();
                fragmentArrayList.add(new SongBoardFragment()) ;
                fragmentArrayList.add(new ArtistBoardFragment());

                TabViewpager2Fragment fragment=new TabViewpager2Fragment(fragmentArrayList);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                break;
            case R.layout.activity_content_artist:

                RecyclerView artistView=findViewById(R.id.artist_list);
                artistView.setHasFixedSize(true);
                artistView.setItemViewCacheSize(20);
                artistView.setDrawingCacheEnabled(true);
                artistView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                LinearLayoutManager layoutManager=new LinearLayoutManager(this);
                artistView.setLayoutManager(layoutManager);

                ChipGroup area=((ChipGroup)findViewById(R.id.artist_area));
                ChipGroup type=((ChipGroup)findViewById(R.id.artist_type));

                Callback callback=new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.code() < 300) {
                            JSONObject currentResults = handle_response(response);
                            JSONArray artistArray = null;
                            List<Music.Artist> artists = new ArrayList<>();
                            try {
                                artistArray = currentResults.getJSONArray("artists");

                                for (int i = 0; i < artistArray.length(); i++) {
                                    JSONObject object = artistArray.getJSONObject(i);
                                    artists.add(new Music.Artist(
                                            object.getLong("id"),
                                            object.getString("name"),
                                            object.getString("picUrl")));
                                };

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Stuff that updates the UI
                                             ArtistFilter.ArtistViewAdapter adapter=new ArtistFilter.ArtistViewAdapter(artists);
                                             artistView.setAdapter(adapter);
                                        }
                                    });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                ResourceUtil.asyncGET(ContentActivity.this,callback,new ArtistFilter.Builder().build().getUrl());

                area.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                        ArtistFilter artistFilter=new ArtistFilter.Builder()
                                .setArea(getCodeByResID(checkedId))
                                .setType(getCodeByResID(type.getCheckedChipId()))
                                .build();
                        String address= artistFilter.getUrl();
                        ResourceUtil.asyncGET(ContentActivity.this,callback,address);
                        //Log.d("#########dd",""+getCodeByResID(checkedId));
                    }
                });
                type.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                        ArtistFilter artistFilter=new ArtistFilter.Builder()
                                .setType(getCodeByResID(checkedId))
                                .setArea(getCodeByResID(area.getCheckedChipId()))
                                .build();
                        String address= artistFilter.getUrl();
                        ResourceUtil.asyncGET(ContentActivity.this,callback,address);
                    }
                });


                break;
            case R.layout.activity_content_classification:
                break;
            case R.layout.activity_content_local:
                break;
            case R.layout.activity_content_download:
                break;

        }
    }

}