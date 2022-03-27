package com.example.necola;

import static com.example.necola.utils.httpAPI.ResourceUtil.handle_response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.entity.Music;
import com.example.necola.fragments.SearchContentFragment;
import com.example.necola.fragments.collections.ArtistBoardFragment;
import com.example.necola.fragments.collections.CollectionFragment;
import com.example.necola.fragments.collections.SongBoardFragment;
import com.example.necola.fragments.collections.TabViewpager2Fragment;
import com.example.necola.utils.httpAPI.ResourceUtil;
import com.example.necola.utils.httpAPI.resource.netease.NeteaseMusicAPI.ResourceUrl;
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
                fragmentArrayList.add(new SongBoardFragment(getString(R.string.song))) ;
                fragmentArrayList.add(new ArtistBoardFragment(getString(R.string.artist)));

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
                                             ArtistViewAdapter adapter=new ArtistViewAdapter(artists);
                                             artistView.setAdapter(adapter);
                                        }
                                    });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                ResourceUtil.asyncGET(ContentActivity.this,callback,new ResourceUrl.Builder("artist/list").build().getUrl());

                area.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                        ResourceUrl  resUrl=new ResourceUrl.Builder("artist/list")
                                .setArea(getCodeByResID(checkedId))
                                .setType(getCodeByResID(type.getCheckedChipId()))
                                .build();

                        ResourceUtil.asyncGET(ContentActivity.this,callback, resUrl.getUrl());
                        //Log.d("#########dd",""+getCodeByResID(checkedId));
                    }
                });
                type.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                        ResourceUrl  resUrl=new ResourceUrl.Builder("artist/list")
                                .setType(getCodeByResID(checkedId))
                                .setArea(getCodeByResID(area.getCheckedChipId()))
                                .build();
                        ResourceUtil.asyncGET(ContentActivity.this,callback,resUrl.getUrl());
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

    static public class ArtistViewAdapter extends RecyclerView.Adapter<ArtistViewAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            ShapeableImageView imageView;
            MaterialTextView textView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.artist_img);
                textView=itemView.findViewById(R.id.artist_name);
            }
        }

        List<Music.Artist> artistList;
        public ArtistViewAdapter(List<Music.Artist> artistList){
            this.artistList=artistList;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_card,parent,false);
            final ViewHolder holder=new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return  holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ArtistViewAdapter.ViewHolder holder, int position) {
            Music.Artist artist=artistList.get(position);
            holder.textView.setText(artist.getName());
            Picasso.get().load(artist.getPicUrl()).fit().into(holder.imageView);
            //new DownloadImageTask(holder.imageView).execute(recommendPlaylist.getPicUrl());

        }

        @Override
        public int getItemCount() {
            return artistList.size();
        }


    }
}