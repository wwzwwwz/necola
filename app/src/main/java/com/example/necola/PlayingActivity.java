package com.example.necola;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.example.necola.service.foreground.MusicConstants;
import com.example.necola.service.foreground.SoundService;
import com.example.necola.service.foreground.SoundStateViewModel;

public class PlayingActivity extends BaseActivity {

    private SoundService mService;
    private Boolean mBound;
    private SoundStateViewModel model;
    //action bar item
    public boolean onOptionsItemSelected(MenuItem item) {
        //code for item select event handling
        switch (item.getItemId()) {
            //android.R 系统内部预先定义好的资源
            case R.id.expand_less:
                onBackPressed();
                return true;
            //R 工程师自定义的资源
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_playing, menu);

        model.getCurrentArtists().observe(PlayingActivity.this,
                s -> menu.findItem(R.id.toolbar_artists).setTitle(s));
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_playing);
        Toolbar mToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Song Namemmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");

        // Bind to LocalService
        Intent soundIntent = new Intent(this, SoundService.class);
        bindService(soundIntent, connectionSource, Context.BIND_AUTO_CREATE);

    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connectionSource = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SoundService.LocalBinder binder = (SoundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            model = mService.getStateViewModel();
            // Create the observer which updates the UI.
            final androidx.lifecycle.Observer<Integer> playStateObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    switch (integer) {
                        case MusicConstants.STATE_SERVICE.PREPARE:
                        case MusicConstants.STATE_SERVICE.PLAY:
                            findViewById(R.id.play_pause).setSelected(true);
                            break;
                        case MusicConstants.STATE_SERVICE.PAUSE:
                        case MusicConstants.STATE_SERVICE.NOT_INIT:
                            findViewById(R.id.play_pause).setSelected(false);
                            break;

                    }
                }
            };


            model.getCurrentPlayerState().observe(PlayingActivity.this, playStateObserver);

            model.getCurrentTitle().observe(PlayingActivity.this,
                    s -> getSupportActionBar().setTitle(s));

            SeekBar seekBar=((SeekBar)(findViewById(R.id.song_progress)));
            model.getCurrentPlayLength().observe(PlayingActivity.this,
                    s -> seekBar.setProgress(s));


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if(b){
                        mService.mPlayer.seekTo((int)((i/100.0)*mService.mPlayer.getDuration()));

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });




        }

        ;
    };

}

