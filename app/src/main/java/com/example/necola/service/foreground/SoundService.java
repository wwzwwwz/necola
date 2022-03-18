package com.example.necola.service.foreground;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;



import android.media.AudioManager;
import android.os.Handler;

import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.necola.FragmentTabActivity;
import com.example.necola.R;
import com.example.necola.entity.Music;

import java.util.ArrayList;
import java.util.Random;


public class SoundService extends Service implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener{

    private final static String FOREGROUND_CHANNEL_ID = "foreground_channel_id";
    private final static String TAG = SoundService.class.getSimpleName();


    private final Object mLock = new Object();
    private final Handler mHandler = new Handler();
    public MediaPlayer mPlayer;

    private Uri mUriRadio;
    private NotificationManager mNotificationManager;
    private final Uri mUriRadioDefault = Uri.parse("https://www.ytmp3.cn/down/75838.mp3");

    private SoundStateViewModel stateViewModel;
    public SoundStateViewModel getStateViewModel(){return stateViewModel;}
    // Binder given to clients
    private final IBinder binder=new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    private Handler mTimerUpdateHandler = new Handler();


    private Runnable mTimerUpdateRunnable = new Runnable() {

        @Override
        public void run() {

            mNotificationManager.notify(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE,
                    prepareNotification());
            mTimerUpdateHandler.postDelayed(this,
                    MusicConstants.DELAY_UPDATE_NOTIFICATION_FOREGROUND_SERVICE);
        }
    };

    private Runnable mTimerUpdateSeek=new Runnable() {
        @Override
        public void run() {
            stateViewModel.getCurrentPlayLength().setValue((int) (mPlayer.getCurrentPosition() / (double) mPlayer.getDuration() * 100));
           // Log.d("Test", "Player progress:" + (int) (mPlayer.getCurrentPosition() / (double) mPlayer.getDuration() * 100));
            mTimerUpdateHandler.postDelayed(this, 0);
            //Log.d("Thread seek","p:"+mPlayer.getCurrentPosition()+",D:"+mPlayer.getDuration());

            if (!mPlayer.isPlaying()) {
                mTimerUpdateHandler.removeCallbacks(this);
                Intent startIntent=new Intent(SoundService.this,SoundService.class);
                startIntent.setAction(MusicConstants.ACTION.PAUSE_ACTION);
                startService(startIntent);
                Log.d("Thread seek","end...");

            }

        }


    };
    private Runnable mDelayedShutdown = new Runnable() {

        public void run() {
            stopForeground(true);
            stopSelf();
        }

    };





    public SoundService() {
    }




    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SoundService.class.getSimpleName(), "onCreate()");

        stateViewModel=new SoundStateViewModel();
        stateViewModel.getCurrentPlayerState().setValue(MusicConstants.STATE_SERVICE.NOT_INIT);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (intent == null || intent.getAction() == null) {
            stopForeground(true);
            stopSelf();

            return START_NOT_STICKY;
        }
        switch (intent.getAction()) {

            case MusicConstants.ACTION.START_ACTION:
                Log.i(TAG, "Received start Intent ");

                stateViewModel.getCurrentPlayerState().setValue(MusicConstants.STATE_SERVICE.PREPARE);

                if (intent.hasExtra("song"))
                {
                    Music.Song song=(Music.Song) intent.getSerializableExtra("song");
                    mUriRadio= Uri.parse(song.getSongData().getUrl());
                    stateViewModel.getCurrentTitle().setValue(song.getTitle());
                    stateViewModel.getCurrentArtists().setValue(song.getArrayListToString());
                   // stateViewModel.getCurrentPicUrl().setValue(song.getPicUrl());
                }
                else{

                    mUriRadio = mUriRadioDefault;
                    stateViewModel.getCurrentTitle().setValue("None");
                    stateViewModel.getCurrentArtists().setValue("None");
                }


                startForeground(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());
                destroyPlayer();
                initPlayer();
                play();

                break;

            case MusicConstants.ACTION.PAUSE_ACTION:
                stateViewModel.getCurrentPlayerState().setValue(MusicConstants.STATE_SERVICE.PAUSE);
                mNotificationManager.notify(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE,
                        prepareNotification());
                Log.i(TAG, "Clicked Pause");
                //destroyPlayer();
                mPlayer.pause();
                //mTimerUpdateHandler.removeCallbacks(mTimerUpdateSeek);
                //lastPostion=mPlayer.getCurrentPosition();

                //mHandler.postDelayed(mDelayedShutdown, MusicConstants.DELAY_SHUTDOWN_FOREGROUND_SERVICE);
                break;

            case MusicConstants.ACTION.PLAY_ACTION:
                stateViewModel.getCurrentPlayerState().setValue(MusicConstants.STATE_SERVICE.PREPARE);
                //currentPlayerState = MusicConstants.STATE_SERVICE.PREPARE;
                mNotificationManager.notify(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());
                Log.i(TAG, "Clicked Play");
                //destroyPlayer();
                if (mPlayer!=null) {
                    mPlayer.seekTo((int)(stateViewModel.getCurrentPlayLength().getValue()/100.0*mPlayer.getDuration()));
                    mPlayer.start();
                    //mTimerUpdateHandler.postDelayed(mTimerUpdateSeek,0);
                }
                else{
                    destroyPlayer();
                    initPlayer();
                    play();
                    //mTimerUpdateHandler.postDelayed(mTimerUpdateSeek,0);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (!mTimerUpdateHandler.hasCallbacks(mTimerUpdateSeek))
                        mTimerUpdateHandler.postDelayed(mTimerUpdateSeek,0);
                    break;
                }

            case MusicConstants.ACTION.STOP_ACTION:
                Log.i(TAG, "Received Stop Intent");
                destroyPlayer();
                stopForeground(true);
                stopSelf();

                break;

            default:
                stopForeground(true);
                stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        destroyPlayer();
        stateViewModel.getCurrentPlayerState().setValue(MusicConstants.STATE_SERVICE.NOT_INIT);
        try {
            mTimerUpdateHandler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void destroyPlayer() {
        if (mPlayer != null) {
            try {
                mPlayer.reset();
                mPlayer.release();
                Log.d(TAG, "Player destroyed");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPlayer = null;
            }
        }


    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "Player onError() what:" + what);
        destroyPlayer();
        mHandler.postDelayed(mDelayedShutdown, MusicConstants.DELAY_SHUTDOWN_FOREGROUND_SERVICE);
        mNotificationManager.notify(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());
        stateViewModel.getCurrentPlayerState().setValue(MusicConstants.STATE_SERVICE.PAUSE);
        return false;
    }

    private void initPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "Player onInfo(), what:" + what + ", extra:" + extra);
                return false;
            }
        });
    }

    private void play() {
        try {
            mHandler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        synchronized (mLock) {
            try {
                if (mPlayer == null) {
                    initPlayer();
                }
                mPlayer.reset();
                mPlayer.setVolume(1.0f, 1.0f);
                mPlayer.setDataSource(this, mUriRadio);
                mPlayer.prepareAsync();
                //mTimerUpdateHandler.postDelayed(mTimerUpdateSeek,0);

            } catch (Exception e) {
                destroyPlayer();
                e.printStackTrace();
            }
        }
    }

    private Notification prepareNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O &&
                mNotificationManager.getNotificationChannel(FOREGROUND_CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            CharSequence name = stateViewModel.getCurrentTitle().getValue();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(FOREGROUND_CHANNEL_ID, name, importance);
            mChannel.setSound(null, null);
            mChannel.enableVibration(false);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        Intent notificationIntent = new Intent(this, FragmentTabActivity.class);
        notificationIntent.setAction(MusicConstants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent lPauseIntent = new Intent(this, SoundService.class);
        lPauseIntent.setAction(MusicConstants.ACTION.PAUSE_ACTION);
        PendingIntent lPendingPauseIntent = PendingIntent.getService(this, 0,
                lPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, SoundService.class);
        playIntent.setAction(MusicConstants.ACTION.PLAY_ACTION);
        PendingIntent lPendingPlayIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent lStopIntent = new Intent(this, SoundService.class);
        lStopIntent.setAction(MusicConstants.ACTION.STOP_ACTION);
        PendingIntent lPendingStopIntent = PendingIntent.getService(this, 0, lStopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews lRemoteViews = new RemoteViews(getPackageName(), R.layout.radio_notification);
        lRemoteViews.setOnClickPendingIntent(R.id.ui_notification_close_button, lPendingStopIntent);

        switch (stateViewModel.getCurrentPlayerState().getValue()) {

            case MusicConstants.STATE_SERVICE.PAUSE:
                lRemoteViews.setViewVisibility(R.id.ui_notification_progress_bar, View.INVISIBLE);
                lRemoteViews.setOnClickPendingIntent(R.id.ui_notification_player_button, lPendingPlayIntent);
                lRemoteViews.setImageViewResource(R.id.ui_notification_player_button, R.drawable.baseline_play_arrow_24);
                break;

            case MusicConstants.STATE_SERVICE.PLAY:
                lRemoteViews.setViewVisibility(R.id.ui_notification_progress_bar, View.INVISIBLE);
                lRemoteViews.setOnClickPendingIntent(R.id.ui_notification_player_button, lPendingPauseIntent);
                lRemoteViews.setImageViewResource(R.id.ui_notification_player_button, R.drawable.baseline_pause_24);
                break;

            case MusicConstants.STATE_SERVICE.PREPARE:
                lRemoteViews.setTextViewText(R.id.sound_music_title,stateViewModel.getCurrentTitle().getValue());
                lRemoteViews.setTextViewText(R.id.sound_music_artists,stateViewModel.getCurrentArtists().getValue());
                lRemoteViews.setViewVisibility(R.id.ui_notification_progress_bar, View.VISIBLE);
                lRemoteViews.setOnClickPendingIntent(R.id.ui_notification_player_button, lPendingPauseIntent);
                lRemoteViews.setImageViewResource(R.id.ui_notification_player_button, R.drawable.baseline_pause_24);
                break;
        }

        NotificationCompat.Builder lNotificationBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            lNotificationBuilder = new NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID);
        } else {
            lNotificationBuilder = new NotificationCompat.Builder(this);
        }
        lNotificationBuilder
                .setContent(lRemoteViews)
                .setSmallIcon(R.drawable.outline_music_note_24)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            lNotificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }
        return lNotificationBuilder.build();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "Player onPrepared()");
        stateViewModel.getCurrentPlayerState().setValue(MusicConstants.STATE_SERVICE.PLAY);
        mNotificationManager.notify(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());

        mPlayer.start();
        mTimerUpdateHandler.postDelayed(mTimerUpdateRunnable, 0);
        mTimerUpdateHandler.postDelayed(mTimerUpdateSeek,0);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d(TAG, "Player onBufferingUpdate():" + percent);
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SoundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


}