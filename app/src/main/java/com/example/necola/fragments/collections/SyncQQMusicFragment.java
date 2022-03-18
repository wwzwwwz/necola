package com.example.necola.fragments.collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.necola.R;

public class SyncQQMusicFragment extends CollectionFragment{
    @Override
    public String getName() {
        return "QQ Music";
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedINstanceState){


        View view=inflater.inflate(R.layout.fragment_aggregation_qq_music,containter,false);


        return view;
    }
}
