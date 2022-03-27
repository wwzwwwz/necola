package com.example.necola.fragments.collections;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.necola.AggregationActivity;
import com.example.necola.R;
import com.example.necola.utils.httpAPI.resource.Auth;
import com.example.necola.utils.httpAPI.resource.netease.NeteaseMusicAPI;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

public class SyncNeteaseMusicFragment extends CollectionFragment{
    String name;
    public SyncNeteaseMusicFragment(String name){
        this.name=name;

    }
    @Override
    public String getName() {
        return this.name;
    }



    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup containter,
                             Bundle savedINstanceState){


        View view=inflater.inflate(R.layout.fragment_aggregation_netease_music_sync,containter,false);

        Button sync=view.findViewById(R.id.sync_third_party);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText account=getActivity().findViewById(R.id.account_input);
                TextInputEditText password=getActivity().findViewById(R.id.password_input);
                NeteaseMusicAPI.syncByLoginNetease(getActivity(),account.getText().toString(),password.getText().toString());
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
/*
        Button sync=view.findViewById(R.id.sync_third_party);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText account=view.findViewById(R.id.account);
                TextInputEditText password=view.findViewById(R.id.password);
                NeteaseMusicAPI.syncByLoginNetease(getActivity(),account.getText().toString(),password.getText().toString());
            }
        });

 */
    }


}
