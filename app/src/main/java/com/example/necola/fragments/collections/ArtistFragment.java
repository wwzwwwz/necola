package com.example.necola.fragments.collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.necola.R;

public class ArtistFragment extends CollectionFragment {

    public static final ArtistFragment newInstance(int title)
    {
        ArtistFragment fragment = new ArtistFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt("title", title);
        fragment.setArguments(bundle);
        return fragment ;
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedINstanceState){


        View view=inflater.inflate(R.layout.fragment_favorites,containter,false);


        return view;
    }


}
