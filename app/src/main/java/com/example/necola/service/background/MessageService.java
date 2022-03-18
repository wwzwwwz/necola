package com.example.necola.service.background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.ChatActivity;
import com.example.necola.R;
import com.example.necola.RoomDBs.model.MsgViewModel;
import com.example.necola.entity.Msg;
import com.example.necola.utils.SocketIOClient;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MessageService extends Service {
    protected Socket mSocket;
    protected String currentUsername;
    protected String currentSender;
    public Msg currentMsg;
    protected MsgViewModel msgViewModel;

    //调用
    int NOTIFICATION_ID = 555;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();



    private Emitter.Listener onForceOffline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            Intent intent = new Intent("com.example.section2.FORCE_OFFLINE");
            sendBroadcast(intent);
            mSocket.disconnect();
            mSocket.off();
        }

    };


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            JSONObject data = (JSONObject) args[0];
            String username="default";
            String message;
            try {
                //username = data.getString("username");
                message = data.getString("message");
                currentSender=data.getString("sender");
            } catch (JSONException e) {
                return;
            }
            //在服务中调用
            startForeground(NOTIFICATION_ID, buildNotification("来自"+currentSender+"的消息",message));

            // add the message to model,model update view
            currentMsg=new Msg(message,Msg.TYPE_RECEIVED,currentUsername,currentSender);
            msgViewModel.insert(currentMsg);//Chat活动中的LiveData观察者帮助我更新UI线程，所以我放心地让后台服务在子线程中更新数据
        }


    };


    public MessageService() {

    }


    // Invoked when the service is started.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent!=null) {


            //Create a child thread.
            msgViewModel = new MsgViewModel(getApplication());
            currentUsername = intent.getStringExtra("currentUsername");
            mSocket = SocketIOClient.getInstance();
            mSocket.on("new message_server", onNewMessage);
            mSocket.on("force_offline", onForceOffline);
            //Create a child thread.
        /*Thread childThread = new Thread()
        {
            @Override
            public void run() {
                try {
                    mSocket= SocketIOClient.getInstance();
                    mSocket.on("new message_server", onNewMessage);
                    Thread currThread = Thread.currentThread();
                    currThread.sleep(1000);
                }catch(InterruptedException ex)
                {

                }
            }
        };
        // Start the child thread.
        childThread.start();
        */
        }
            return super.onStartCommand(intent, flags, startId);

    }



    private static final String NOTIFICATION_CHANNEL_ID = "RECEIVE_CONTACT_MESSAGE";

    private Notification buildNotification(String title, String msg) {

        final String contentTitle = title;
        final String contentText = msg;

        // final String intentAction = mVisibleWindow ? ACTION_HIDE : ACTION_SHOW;
        Intent actionIntent = new Intent(this, ChatActivity.class);
        actionIntent.putExtra("currentUsername",currentUsername);
        actionIntent.putExtra("currentContact",currentSender);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification.Builder builder = new Notification.Builder(this).setContentTitle(contentTitle).setContentText(contentText)
                .setPriority(Notification.PRIORITY_MIN).setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//打开通知详情intent
                .setAutoCancel(true)//点击取消
                .setOngoing(false)
                .setShowWhen(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannel();
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        //final int messageId = mVisibleWindow ? R.string.toggle_hide : R.string.toggle_show;
        builder.addAction(android.R.drawable.ic_menu_preferences, "Custom Action", PendingIntent.getService(this, 0, actionIntent, 0));
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNotificationChannel() {
        String channelName = "消息通知";
        String channelDescription = "聊天消息";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
        channel.setDescription(channelDescription);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }




    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public MessageService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MessageService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocket!=null) if(mSocket.connected()) {
            mSocket.disconnect();
            mSocket.off("new message", onNewMessage);
        }
    }

}
