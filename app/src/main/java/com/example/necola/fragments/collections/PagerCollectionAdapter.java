package com.example.necola.fragments.collections;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;


public class PagerCollectionAdapter extends FragmentStateAdapter {

        List<CollectionFragment> fragmentList=new ArrayList<>();


        public void setFragmentList(List<CollectionFragment> fragmentList) {
            this.fragmentList = fragmentList;
        }

        public PagerCollectionAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }


    }