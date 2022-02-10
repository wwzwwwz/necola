package com.example.necola.service.foreground;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.necola.FragmentTabActivity;
import com.example.necola.R;

import java.io.IOException;

public class SoundintentService extends IntentService implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {
    MediaPlayer mediaPlayer;
    private final Uri mUriRadioDefault = Uri.parse("https://www.ytmp3.cn/down/75838.mp3");
    private Uri mUriRadio;
    private NotificationManager mNotificationManager;

    static private int sStateService = MusicConstants.STATE_SERVICE.NOT_INIT;

    private final static String FOREGROUND_CHANNEL_ID = "foreground_channel_id";


    public SoundintentService() {
        super("Play Music");

    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SoundService.class.getSimpleName(), "onCreate()");
        sStateService = MusicConstants.STATE_SERVICE.NOT_INIT;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mUriRadio = mUriRadioDefault;
        mNotificationManager.notify(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());
    }

    public void initMediaPlayer() {
        // ...initialize the MediaPlayer here...
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
    }

    private void play(){
        if (mediaPlayer == null) {
            initMediaPlayer();
        }
        mediaPlayer.reset();
        mediaPlayer.setVolume(1.0f, 1.0f);
        try {
            mediaPlayer.setDataSource(this, mUriRadio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
    }
    private void destroyPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
                //Log.d(TAG, "Player destroyed");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mediaPlayer = null;
            }
        }


    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {



        switch (intent.getAction()) {
            case MusicConstants.ACTION.START_ACTION:
                //Log.i(TAG, "Received start Intent ");
                sStateService = MusicConstants.STATE_SERVICE.PREPARE;

                startForeground(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());
                destroyPlayer();
                initMediaPlayer();
                play();
                break;

            case MusicConstants.ACTION.PAUSE_ACTION:
                sStateService = MusicConstants.STATE_SERVICE.PAUSE;
                mNotificationManager.notify(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());
                //Log.i(TAG, "Clicked Pause");
                destroyPlayer();
                //mHandler.postDelayed(mDelayedShutdown, MusicConstants.DELAY_SHUTDOWN_FOREGROUND_SERVICE);
                break;

            case MusicConstants.ACTION.PLAY_ACTION:
                sStateService = MusicConstants.STATE_SERVICE.PREPARE;
                mNotificationManager.notify(MusicConstants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());
               // Log.i(TAG, "Clicked Play");
                destroyPlayer();
                initMediaPlayer();
                play();
                break;

            case MusicConstants.ACTION.STOP_ACTION:
                //Log.i(TAG, "Received Stop Intent");
                destroyPlayer();
                stopForeground(true);
                stopSelf();
                break;

            default:
                stopForeground(true);
                stopSelf();
        }

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        sStateService = MusicConstants.STATE_SERVICE.PLAY;
        mediaPlayer.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }




    private Notification prepareNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O &&
                mNotificationManager.getNotificationChannel(FOREGROUND_CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            CharSequence name = getString(R.string.text_value_radio_notification);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(FOREGROUND_CHANNEL_ID, name, importance);
            mChannel.setSound(null, null);
            mChannel.enableVibration(false);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        Intent notificationIntent = new Intent(this, FragmentTabActivity.class);
        notificationIntent.setAction(MusicConstants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent lPauseIntent = new Intent(this, SoundService.class);
        lPauseIntent.setAction(MusicConstants.ACTION.PAUSE_ACTION);
        PendingIntent lPendingPauseIntent = PendingIntent.getService(this, 0, lPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, SoundService.class);
        playIntent.setAction(MusicConstants.ACTION.PLAY_ACTION);
        PendingIntent lPendingPlayIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent lStopIntent = new Intent(this, SoundService.class);
        lStopIntent.setAction(MusicConstants.ACTION.STOP_ACTION);
        PendingIntent lPendingStopIntent = PendingIntent.getService(this, 0, lStopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews lRemoteViews = new RemoteViews(getPackageName(), R.layout.radio_notification);
        lRemoteViews.setOnClickPendingIntent(R.id.ui_notification_close_button, lPendingStopIntent);

        switch (sStateService) {

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
                .setSmallIcon(R.mipmap.ic_launcher)
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
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        //Log.d(Tag, "Player onBufferingUpdate():" + i);
    }
}
