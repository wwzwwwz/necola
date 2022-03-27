package com.example.necola.fragments.collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.necola.R;

public class SongBoardFragment extends CollectionFragment {


    String name;
    public SongBoardFragment(String name){
        this.name=name;

    }
    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedINstanceState){


        View view=inflater.inflate(R.layout.fragment_favorites,containter,false);


        return view;
    }


}
