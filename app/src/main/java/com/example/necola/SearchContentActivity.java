package com.example.necola;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.necola.entity.Music;
import com.example.necola.fragments.SearchContentFragment;

public class SearchContentActivity extends BaseActivity {

    public  static void actionStart(Context context, Music.Song song){
        Intent intent=new Intent(context, SearchContentActivity.class);
        intent.putExtra("song_data",song);
        context.startActivity(intent);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_content);
        Music.Song song=(Music.Song) getIntent().getSerializableExtra("song_data");


        SearchContentFragment searchContentFragment =(SearchContentFragment)
                getSupportFragmentManager().findFragmentById(R.id.music_content_fragment);
        searchContentFragment.refresh(song);//刷新 相应Fragment界面
    }

}