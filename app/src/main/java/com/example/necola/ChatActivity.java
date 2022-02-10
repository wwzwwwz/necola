package com.example.necola;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necola.ViewModel.MsgViewModel;
import com.example.necola.adapter.MsgAdapter;
import com.example.necola.entity.Msg;
import com.example.necola.service.MessageService;
import com.example.necola.utils.SocketIOClient;

public class ChatActivity extends BaseActivity{

    public String currentContact;
    int msgSize;
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgViewModel msgViewModel;
    private MsgAdapter adapter;

    private MessageService mService;
    boolean mBound=false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_chat, menu);
        //actionbar.setTitle(currentContact);
        return true;
    }
    //action bar item
    public boolean onOptionsItemSelected(MenuItem item) {
        //code for item select event handling
        switch (item.getItemId()) {
            //android.R 系统内部预先定义好的资源
            case android.R.id.home:
                onBackPressed();
                return true;
            //R 工程师自定义的资源
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar mToolbar=findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        actionbar=getSupportActionBar();
        mToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24);
        currentContact=getIntent().getStringExtra("currentContact");
        currentUsername=getIntent().getStringExtra("currentUsername");

        setTitle(currentContact);
        mSocket= SocketIOClient.getInstance();



        msgRecyclerView=(RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);


        adapter=new MsgAdapter(new MsgAdapter.MsgDiff());
        msgRecyclerView.setAdapter(adapter);
        // Get a new or existing ViewModel from the ViewModelProvider.
        //msgViewModel = new ViewModelProvider(this).get(MsgViewModel.class);
        //实现特定会话查询
        msgViewModel = new MsgViewModel(getApplication(),currentUsername,currentContact);

        //定位到最后一行
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int position, int itemCount) {
                msgSize=msgViewModel.getAllMsgs().getValue().size();
                msgRecyclerView.scrollToPosition(msgSize-1);//ListView定位到最后一行
            }
        });

        //在getAllMsgs返回的LiveData上添加一个观察者。
        // onChanged()方法在观察到的数据发生变化时触发在前台。更新UI
        msgViewModel.getAllMsgs().observe(this,msg->adapter.submitList(msg));

        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText=(EditText) findViewById(R.id.input_text);
                String content =inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg=new Msg(content,Msg.TYPE_SENT,currentUsername,currentContact);
                    msgViewModel.insert(msg); //如果msg的主键缺失，那么就insert就执行失败且不会报错，所以自增primiray id解决
                    inputText.setText("");//清空输入框
                    mSocket.emit("new message_clint", content,currentContact,currentUsername);
                }
            }
        });
    }


    //目前还没有用到绑定服务的功能，MessageService实际上只是一个单纯的后台服务
    @Override
    protected void onStart(){
        super.onStart();
        Intent intent=new Intent(this, MessageService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            mService=((MessageService.LocalBinder)service).getService();
            mBound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
