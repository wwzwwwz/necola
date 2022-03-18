package com.example.necola.fragments.collections;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.necola.AggregationActivity;
import com.example.necola.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class TabViewpager2Fragment extends Fragment {
    ViewPager2 pager;
    PagerCollectionAdapter collectionAdapter;
    List<CollectionFragment> fragmentList;

    public TabViewpager2Fragment(List<CollectionFragment> fragmentList){
        this.fragmentList=fragmentList;
    }
    //TabLayout tabLayout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tablayout_viewpager2, container, false);

        collectionAdapter = new PagerCollectionAdapter(this);
        //initFragmentList();
        collectionAdapter.setFragmentList(fragmentList);

        pager = view.findViewById(R.id.pager);
        pager.setAdapter(collectionAdapter);
        pager.setSaveEnabled(false);// To avoid error: Fragment no longer exists for key f#0
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setText(fragmentList.get(position).getName())
        ).attach();

    }
}
