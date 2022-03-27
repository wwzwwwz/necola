package com.example.necola.fragments.collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.necola.R;

public class CollectedAlbumFragment extends CollectionFragment {

    /*
    String name;
    public CollectedAlbumFragment(String name){
        this.name=name;

    }
    @Override
    public String getName() {
        return this.name;
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedINstanceState){


        View view=inflater.inflate(R.layout.fragment_favorites,containter,false);


        return view;
    }


    public static final CollectedAlbumFragment newInstance(int title)
    {
        CollectedAlbumFragment fragment = new CollectedAlbumFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt("title", title);
        fragment.setArguments(bundle);
        return fragment ;
    }

}
