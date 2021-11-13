package com.example.section2;


import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.section2.fragments.ContactFragment;
import com.example.section2.fragments.ContextFragment;

import android.annotation.SuppressLint;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.example.section2.R;

import com.example.section2.fragments.LoginFragment;
import com.example.section2.fragments.NewsTitleFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class FragmentTabActivity extends BaseActivity{

    public String[] btnTitles = new String[]{"Message","Contact", "Status"};// 按钮标题
    public List<Fragment> contextFragments = new ArrayList<>();// 用来存放Fragments的集合
    public BottomNavigationView bottomNavigationView;
    public ActionBar actionbar;
    private DrawerLayout mDrawer;
    public NavigationView drawerNavigation;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //action bar item
    public boolean onOptionsItemSelected(MenuItem item) {
        //code for item select event handling
        switch (item.getItemId()) {
            //android.R 系统内部预先定义好的资源

            //R 工程师自定义的资源
            case android.R.id.home:
                mDrawer.openDrawer(Gravity.RIGHT);
                return true;
            case R.id.search:
                actionbar.setTitle("first");
                return true;
            case R.id.filter:
                actionbar.setTitle("filter");
            default:
                break;
        }
        return false;
    }
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("TEST");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_tab);
        setSupportActionBar(findViewById(R.id.app_bar));
        actionbar=getSupportActionBar();
        NewsTitleFragment newsTitleFragment=new NewsTitleFragment();
        ContactFragment contactFragment=new ContactFragment();
        mDrawer=(DrawerLayout) findViewById(R.id.drawer_layout) ;
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.page_1:
                        actionbar.setTitle("1");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, contactFragment).commit();
                        return true;
                    case R.id.page_2:
                        actionbar.setTitle("2");
                        break;
                    case R.id.page_3:
                        actionbar.setTitle("3");
                        break;
                }
                return true;
            }
        });




        drawerNavigation=(NavigationView) findViewById(R.id.drawer_navigation);
        drawerNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.first:
                        actionbar.setTitle("BIG");
                        Intent intent=new Intent(FragmentTabActivity.this,NewsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.second:
                        actionbar.setTitle("2");
                        return true;
                    case R.id.third:
                        actionbar.setTitle("3");
                        return true;
                }

                return false;
            }
        });

    }

    public void onClickCalled(String anyValue) {
        // Call another acitivty here and pass some arguments to it.
        actionbar.setTitle("Chat");
        Intent intent=new Intent(FragmentTabActivity.this,ChatActivity.class);
        startActivity(intent);
    }




}