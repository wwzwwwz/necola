package com.example.necola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.necola.fragments.collections.ArtistFragment;
import com.example.necola.fragments.collections.CollectionFragment;
import com.example.necola.fragments.collections.SyncNeteaseMusicFragment;
import com.example.necola.fragments.collections.SyncQQMusicFragment;
import com.example.necola.fragments.collections.TabViewpager2Fragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AggregationActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggregation);

        ArrayList<CollectionFragment> fragmentArrayList=new ArrayList<>();
        fragmentArrayList.add(new SyncNeteaseMusicFragment(getString(R.string.netease_music))) ;
        fragmentArrayList.add(new SyncQQMusicFragment(getString(R.string.qqmusic)));

        TabViewpager2Fragment fragment=new TabViewpager2Fragment(fragmentArrayList);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();



    }


}