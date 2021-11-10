package com.example.section2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.section2.adapter.FruitAdapter;
import com.example.section2.adapter.MsgAdapter;
import com.example.section2.entity.Fruit;
import com.example.section2.entity.Msg;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity{
    private List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initMsgs();
        msgRecyclerView=(RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText=(EditText) findViewById(R.id.input_text);
                String content =inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg=new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);//有新消息时，刷新ListView显示
                    msgRecyclerView.scrollToPosition(msgList.size()-1);//ListView定位到最后一行
                    inputText.setText("");//清空输入框
                }
            }
        });
    }

    private void initMsgs(){
        Msg msg1=new Msg("Hello guy.",Msg.TYPE_RECEIVED);
        Msg msg2=new Msg("Hello Who is that?.",Msg.TYPE_SENT);
        Msg msg3=new Msg("This is Bro.",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        msgList.add(msg2);
        msgList.add(msg3);
        }

}
