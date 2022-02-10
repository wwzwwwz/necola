package com.example.necola;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.necola.entity.Contacts;
import com.example.necola.service.foreground.MusicConstants;
import com.example.necola.service.foreground.SoundService;
import com.example.necola.fragments.ContactFragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import com.example.necola.service.io.DownloadService;
import com.example.necola.utils.ActivityCollector;
import com.example.necola.utils.SocketIOClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class FragmentTabActivity extends BaseActivity{

    public String[] btnTitles = new String[]{"Message","Contact", "Status"};// 按钮标题
    public List<Fragment> contextFragments = new ArrayList<>();// 用来存放Fragments的集合

    public String m_Text;
    public ContactFragment contactFragment;

    public BottomNavigationView bottomNavigationView;
    private DrawerLayout mDrawer;
    public NavigationView drawerNavigation;


    private Emitter.Listener onNewChat= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            FragmentTabActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String currentSender;
                    JSONObject data = (JSONObject) args[0];

                    try {
                        currentSender=data.getString("sender");
                    } catch (JSONException e) {
                        return;
                    }
                    Contacts he=new Contacts(currentSender,R.drawable.strawberry);
                    if(!contactFragment.contactList.contains(he))
                        {contactFragment.addContacts(currentSender);
                         contactFragment.refresh();}
                }
            });
        }
    };

    void PlaySheetListener(){


        Button playSheet=findViewById(R.id.sheet_play);
        playSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent=new Intent(
                        FragmentTabActivity.this,
                        SoundService.class);
                startIntent.setAction(MusicConstants.ACTION.START_ACTION);
                startService(startIntent);
                

            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);//返回键功能变成挂起了
    }

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
            case android.R.id.home:
                mDrawer.openDrawer(Gravity.RIGHT);
                return true;
            case R.id.search:
                Intent intent=new Intent(FragmentTabActivity.this, SearchActivity.class);
                startActivity(intent);
            case R.id.option_2:
                //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //startActivity(intent);
                return true;
            case R.id.option_1:
                MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(FragmentTabActivity.this)
                        .setTitle("Please Input Username");
                // Set up the input

                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                materialAlertDialogBuilder.setView(input);

// Set up the buttons
                materialAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Intent intent=new Intent(FragmentTabActivity.this,ChatActivity.class);
                        intent.putExtra("currentUsername",currentUsername);
                        intent.putExtra("currentContact",m_Text);
                        startActivity(intent);

                        Contacts he=new Contacts(m_Text,R.drawable.strawberry);
                        if(!contactFragment.contactList.contains(he))
                            contactFragment.addContacts(m_Text);

                    }
                });
                materialAlertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                materialAlertDialogBuilder.show();


            default:
                break;
        }
        return false;
    }

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder=(DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("TEST");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tab);
        mSocket= SocketIOClient.getInstance();
        mSocket.on("new chat_server",onNewChat);
        currentUsername=getIntent().getStringExtra("currentUsername");

        //必须在setContentView后才能setSupportActionBar，否则设置失败
        Toolbar mToolbar=findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        actionbar=getSupportActionBar();
        mToolbar.setNavigationIcon(R.drawable.menu);

        contactFragment=new ContactFragment();
        mDrawer=(DrawerLayout) findViewById(R.id.drawer_layout) ;
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        PlaySheetListener();

        actionbar.setTitle("Contact");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, contactFragment).commit();

        Intent downloadIntent=new Intent(this,DownloadService.class);
        startService(downloadIntent);//

        bindService(downloadIntent,connection,BIND_AUTO_CREATE);//绑定服务

        if(ContextCompat.checkSelfPermission(FragmentTabActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(FragmentTabActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.page_1:
                        actionbar.setTitle("Contact");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, contactFragment).commit();
                        return true;
                    case R.id.page_2:
                        actionbar.setTitle("Status");
                        break;
                    case R.id.page_3:
                        actionbar.setTitle("More");
                        break;
                }
                return true;
            }
        });




        drawerNavigation=(NavigationView) findViewById(R.id.drawer_navigation);

        drawerNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.first:
                        String url="https://www.ytmp3.cn/down/75838.mp3";
                        downloadBinder.startDownload(url);
                        return true;
                    case R.id.second:
                        downloadBinder.pauseDownload();
                        return true;
                    case R.id.third:
                        downloadBinder.cancelDownload();
                        return true;
                    case R.id.fourth:
                        actionbar.setTitle("test webview");
                        Intent intent2=new Intent(FragmentTabActivity.this,WebViewActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.logout:
                        mSocket.disconnect();
                        mSocket.off();
                        ActivityCollector.finishAll();
                        Intent loginIntent=new Intent(FragmentTabActivity.this,LoginActivity.class);
                        startActivity(loginIntent);
                        return true;
                    case R.id.test_news:
                        Intent intent=new Intent(FragmentTabActivity.this, SearchActivity.class);
                        startActivity(intent);
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
        intent.putExtra("currentContact",anyValue);
        intent.putExtra("currentUsername",currentUsername);
        startActivity(intent);
    }




}