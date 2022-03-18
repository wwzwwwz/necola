package com.example.necola.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.necola.R;
import com.example.necola.entity.Music;

public class SearchContentFragment extends Fragment   {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedINstanceState){
        view=inflater.inflate(R.layout.fragment_search_content,containter,false);
        return view;
    }

    public void refresh(Music.Song song){
        View visibilityLayout=view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView newsTitleText=view.findViewById(R.id.music_title);
        //TextView newsContentText=(TextView) view.findViewById(R.id.music_content_layout);
        if (song!=null)
        newsTitleText.setText(song.getTitle());
        else
            newsTitleText.setText("None");
    }
}
