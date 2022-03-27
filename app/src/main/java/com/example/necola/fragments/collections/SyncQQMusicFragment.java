package com.example.necola.fragments.collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.necola.R;

public class SyncQQMusicFragment extends CollectionFragment{
    String name;
    public SyncQQMusicFragment(String name){
        this.name=name;

    }
    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedINstanceState){


        View view=inflater.inflate(R.layout.fragment_aggregation_qq_music,containter,false);


        return view;
    }
}
