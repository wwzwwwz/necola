package com.example.necola.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.R;
import com.example.necola.adapter.ContactAdapter;
import com.example.necola.entity.Contacts;

import java.util.ArrayList;
import java.util.List;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ContactFragment extends Fragment {


        private String[] data={"apple","Banana","Pear","Orange"};
        public List<Contacts> contactList=new ArrayList<>();
        ContactAdapter contactAdapter;
        RecyclerView recyclerView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_contact, container, false);


            initContacts();
            recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager=new LinearLayoutManager((getActivity()));
            recyclerView.setLayoutManager(layoutManager);
            contactAdapter=new ContactAdapter(contactList);
            recyclerView.setAdapter(contactAdapter);



            return view;
        }
        public void refresh(){

            recyclerView.setAdapter(contactAdapter);
        }
        private void initContacts(){

            Contacts me=new Contacts("test",R.drawable.apple);
            contactList.add(me);

            Contacts he=new Contacts("test1",R.drawable.blackberry);
            contactList.add(he);


            }

    public void addContacts(String username){

        Contacts me=new Contacts(username,R.drawable.strawberry);
        contactList.add(me);

    }

        }
