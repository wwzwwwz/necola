package com.example.necola.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.viewpager2.widget.ViewPager2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.necola.AggregationActivity;
import com.example.necola.FragmentTabActivity;
import com.example.necola.R;
import com.example.necola.fragments.collections.CollectedAlbumFragment;
import com.example.necola.fragments.collections.ArtistFragment;
import com.example.necola.fragments.collections.CollectionFragment;
import com.example.necola.fragments.collections.OriginalPlaylistFragment;
import com.example.necola.fragments.collections.PagerCollectionAdapter;
import com.example.necola.fragments.collections.CollectedPlaylistsFragment;
import com.example.necola.fragments.dialog.RecentAlbumListDialogFragment;
import com.example.necola.fragments.dialog.RecentPlaySongListDialogFragment;
import com.example.necola.fragments.dialog.RecentPlaylistDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class MeFragment extends Fragment {

    ViewPager2 pager;
    PagerCollectionAdapter collectionAdapter;

    // every fragment object must specify 'tag' filed in its layout file
    List<CollectionFragment> fragmentList;
    //TabLayout tabLayout

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        collectionAdapter=new PagerCollectionAdapter(this);
        initFragmentList();
        collectionAdapter.setFragmentList(fragmentList);


        pager=view.findViewById(R.id.pager);
        pager.setAdapter(collectionAdapter);
        pager.setSaveEnabled(false);// To avoid error: Fragment no longer exists for key f#0


        return view;
    }
    void initFragmentList(){


        fragmentList=new ArrayList<>();
        fragmentList.add(new OriginalPlaylistFragment());
        fragmentList.add(new CollectedPlaylistsFragment());
        fragmentList.add(new CollectedAlbumFragment());
        fragmentList.add(new ArtistFragment());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setText(fragmentList.get(position).getName())
        ).attach();

        Button sync=view.findViewById(R.id.sync_third_party);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AggregationActivity.class));
            }
        });

        MaterialCardView recentSong=view.findViewById(R.id.recent_song);
        recentSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentPlaySongListDialogFragment.newInstance(30).show(getActivity().getSupportFragmentManager(), "dialog");

            }
        });

        MaterialCardView recentPlaylist=view.findViewById(R.id.recent_playlist);
        recentPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentPlaylistDialogFragment.newInstance(30).show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        MaterialCardView recentAlbum=view.findViewById(R.id.recent_album);
        recentAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentAlbumListDialogFragment.newInstance(30).show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
    }








}

