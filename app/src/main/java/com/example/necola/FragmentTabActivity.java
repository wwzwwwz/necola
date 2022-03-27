package com.example.necola;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.example.necola.fragments.DiscoverFragment;
import com.example.necola.fragments.MeFragment;
import com.example.necola.fragments.StatusFragment;
import com.example.necola.opengles.OpenGLES3;
import com.example.necola.service.foreground.MusicConstants;
import com.example.necola.service.foreground.SoundService;
import com.example.necola.fragments.ContactFragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.necola.service.foreground.SoundStateViewModel;
import com.example.necola.service.io.DownloadService;
import com.example.necola.utils.ActivityCollector;
import com.example.necola.utils.SocketIOClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class FragmentTabActivity extends BaseActivity{

    protected int currentNavItemId;
    protected MeFragment meFragment;
    public ContactFragment contactFragment;
    public DiscoverFragment discoverFragment;
    protected StatusFragment statusFragment;

    public BottomNavigationView bottomNavigationView;
    private DrawerLayout mDrawer;
    public NavigationView drawerNavigation;

    SoundService mService;
    boolean mBound = false;
    String NAV_ITEM_TAG_DISCOVER;
    String NAV_ITEM_TAG_ME;
    String NAV_ITEM_TAG_MESSAGE;
    FragmentManager fragmentManager;

    private Emitter.Listener onNewChat= args -> FragmentTabActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            String currentSender;
            JSONObject data = (JSONObject) args[0];

            try {
                currentSender=data.getString("sender");
            } catch (JSONException e) {
                return;
            }
        }
    });

    void initPlaySheet(){


        ShapeableImageView play=findViewById(R.id.playbar_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent=new Intent(
                        FragmentTabActivity.this,
                        SoundService.class);

                switch (mService.getStateViewModel().getCurrentPlayerState().getValue()){


                    case MusicConstants.STATE_SERVICE.NOT_INIT:
                        startIntent.setAction(MusicConstants.ACTION.START_ACTION);
                        break;
                    case MusicConstants.STATE_SERVICE.PAUSE:
                        startIntent.setAction(MusicConstants.ACTION.PLAY_ACTION);
                        break;
                    case MusicConstants.STATE_SERVICE.PLAY:
                    case MusicConstants.STATE_SERVICE.PREPARE:
                        startIntent.setAction(MusicConstants.ACTION.PAUSE_ACTION);
                        break;

                };
                startService(startIntent);
                // play.setSelected(true);





            }
        });

        ///////////////////queue////////////////////////

        ShapeableImageView queue=findViewById(R.id.playbar_queue);
        queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog dialog = new BottomSheetDialog(FragmentTabActivity.this);
                dialog.setContentView(R.layout.dialog_bottom_sheet_playqueue);

                dialog.show();
            }
        });

        ImageView detail=findViewById(R.id.playbar_img);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FragmentTabActivity.this, PlayingActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });

        /////////search///////
        ShapeableImageView search=findViewById(R.id.playbar_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FragmentTabActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();

        //unbindService(connectionS);
        // mBound = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connectionSource = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SoundService.LocalBinder binder = (SoundService.LocalBinder)service;
            mService = binder.getService();
            mBound = true;
            SoundStateViewModel model=mService.getStateViewModel();
            // Create the observer which updates the UI.
            final androidx.lifecycle.Observer<Integer> playStateObserver=new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    switch (integer) {
                        case MusicConstants.STATE_SERVICE.PREPARE:
                        case MusicConstants.STATE_SERVICE.PLAY:
                            findViewById(R.id.playbar_play).setSelected(true);
                            break;
                        case MusicConstants.STATE_SERVICE.PAUSE:
                        case MusicConstants.STATE_SERVICE.NOT_INIT:
                            findViewById(R.id.playbar_play).setSelected(false);
                            break;

                    }
                }
            };


            model.getCurrentPlayerState().observe(FragmentTabActivity.this,playStateObserver);

            model.getCurrentTitle().observe(FragmentTabActivity.this,
                    s -> ((TextView)findViewById(R.id.playbar_title)).setText((String) s));

            model.getCurrentArtists().observe(FragmentTabActivity.this,
                    s ->  ((TextView)findViewById(R.id.playbar_artists)).setText((String) s));

            model.getCurrentPicUrl().observe(FragmentTabActivity.this,
                    s -> Picasso.get().load(s).into((ShapeableImageView)findViewById(R.id.playbar_img)));


            /////

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


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
            case R.id.more:
                mDrawer.openDrawer(Gravity.RIGHT);
                return true;
            default:
                break;
        }
        return false;
    }

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connectionDownload=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder=(DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putInt("currentNavItemId", currentNavItemId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tab);
        NAV_ITEM_TAG_DISCOVER=getString(R.string.discover);
        NAV_ITEM_TAG_ME=getString(R.string.me);
        NAV_ITEM_TAG_MESSAGE=getString(R.string.message);

        mSocket= SocketIOClient.getInstance();
        mSocket.on("new chat_server",onNewChat);
        currentUsername=getIntent().getStringExtra("currentUsername");

        //必须在setContentView后才能setSupportActionBar，否则设置失败
        Toolbar mToolbar=findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        actionbar=getSupportActionBar();
        //mToolbar.setNavigationIcon(R.drawable.menu);

        meFragment=new MeFragment();
        statusFragment=new StatusFragment();
        contactFragment=new ContactFragment();
        discoverFragment=new DiscoverFragment();
        mDrawer=(DrawerLayout) findViewById(R.id.drawer_layout) ;
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        //getSupportFragmentManager().
        initPlaySheet();





        //startService(downloadIntent);//

        // Bind to LocalService
        Intent soundIntent = new Intent(this, SoundService.class);
        bindService(soundIntent, connectionSource, Context.BIND_AUTO_CREATE);

        Intent downloadIntent=new Intent(this,DownloadService.class);
        bindService(downloadIntent,connectionDownload,BIND_AUTO_CREATE);//绑定服务

        if(ContextCompat.checkSelfPermission(FragmentTabActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(FragmentTabActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }


        fragmentManager=getSupportFragmentManager();

        if(savedInstanceState!=null)
        {
            currentNavItemId= savedInstanceState.getInt("currentNavItemId");


            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commitNow();
            }

            fragmentManager.beginTransaction().add(R.id.container, discoverFragment,NAV_ITEM_TAG_DISCOVER).hide(discoverFragment).commitNow();
            fragmentManager.beginTransaction().add(R.id.container, contactFragment,NAV_ITEM_TAG_MESSAGE).hide(contactFragment).commitNow();
            fragmentManager.beginTransaction().add(R.id.container, meFragment,NAV_ITEM_TAG_ME).hide(meFragment).commitNow();
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(getString(currentNavItemId))).commit();//normal working need above transactions to be commitNow

        }
        else{
            fragmentManager.beginTransaction().add(R.id.container, discoverFragment,NAV_ITEM_TAG_DISCOVER).commit();
            fragmentManager.beginTransaction().add(R.id.container, contactFragment,NAV_ITEM_TAG_MESSAGE).hide(contactFragment).commit();
            fragmentManager.beginTransaction().add(R.id.container, meFragment,NAV_ITEM_TAG_ME).hide(meFragment).commit();
            currentNavItemId =R.string.discover;

        }

        actionbar.setTitle(getString(currentNavItemId));




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {

                    case R.id.discover:
                        actionbar.setTitle(getString(R.string.discover));

                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(getString(currentNavItemId))).show(fragmentManager.findFragmentByTag(NAV_ITEM_TAG_DISCOVER)).commit();
                        currentNavItemId =R.string.discover;
                        return true;
                    case R.id.chat:
                        actionbar.setTitle(getString(R.string.message));
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(getString(currentNavItemId))).show(fragmentManager.findFragmentByTag(NAV_ITEM_TAG_MESSAGE)).commit();
                        currentNavItemId =R.string.message;
                        return true;
                    // case R.id.status:
                    //   actionbar.setTitle("Status");
                    // getSupportFragmentManager().beginTransaction().replace(R.id.container, statusFragment).commit();
                    //break;
                    case R.id.me:
                        actionbar.setTitle(getString(R.string.me));
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(getString(currentNavItemId))).show(fragmentManager.findFragmentByTag(NAV_ITEM_TAG_ME)).commit();
                        currentNavItemId =R.string.me;
                        break;
                }

                return true;
            }
        });




        drawerNavigation=(NavigationView) findViewById(R.id.drawer_navigation);

        drawerNavigation.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.first:
                    String url="http://m8.music.126.net/20220222223759/b2ea65ee5119c4306d768dc7ae6ada54/ymusic/85a5/04ca/e565/5b5777c9264739e0db614ca8d6b01588.mp3";
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
                    SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                    ActivityCollector.finishAll();

                    editor.putBoolean("isSavedLogin",false);
                    editor.apply();
                    Intent surfaceIntent=new Intent(FragmentTabActivity.this,LoggedOutActivity.class);
                    startActivity(surfaceIntent);
                    return true;
                case R.id.opengl3:
                    Intent intent=new Intent(FragmentTabActivity.this, OpenGLES3.class);
                    startActivity(intent);
                    return true;
            }

            return false;
        });

    }

    public void onClickCalled(String anyValue) {
        // Call another acitivty here and pass some arguments to it.
        Intent intent=new Intent(FragmentTabActivity.this,ChatActivity.class);
        intent.putExtra("currentContact",anyValue);
        intent.putExtra("currentUsername",currentUsername);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}