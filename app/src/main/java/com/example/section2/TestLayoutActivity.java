package com.example.section2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.section2.adapter.FruitAdapter;
import com.example.section2.entity.Fruit;

import java.util.ArrayList;
import java.util.List;

public class TestLayoutActivity extends AppCompatActivity {

    private String[] data={"apple","Banana","Pear","Orange"};
    private List<Fruit> fruitList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);
        /*
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,data);
        ListView listView=(ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter); */

        initFruits();
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FruitAdapter adapter=new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
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