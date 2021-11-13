package com.example.section2.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.section2.R;
import com.example.section2.adapter.FruitAdapter;
import com.example.section2.entity.Fruit;

import java.util.ArrayList;
import java.util.List;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.section2.adapter.FruitAdapter;
import com.example.section2.entity.Fruit;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {


        private String[] data={"apple","Banana","Pear","Orange"};
        private List<Fruit> fruitList=new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.activity_test_layout, container, false);

        /*
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,data);
        ListView listView=(ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter); */

            initFruits();
            RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager=new LinearLayoutManager((getActivity()));
            recyclerView.setLayoutManager(layoutManager);
            FruitAdapter adapter=new FruitAdapter(fruitList);
            recyclerView.setAdapter(adapter);



            return view;
        }

        private void initFruits(){
            for(int i=0;i<10;i++){
                Fruit apple=new Fruit("Apple",R.drawable.apple);
                fruitList.add(apple);
                Fruit blackberry=new Fruit("Blackberry",R.drawable.blackberry);
                fruitList.add(blackberry);
                Fruit cherries=new Fruit("Cherries",R.drawable.cherries);
                fruitList.add(cherries);

            }
        }
    }