package com.example.section2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.section2.ViewModel.MsgViewModel;
import com.example.section2.adapter.FruitAdapter;
import com.example.section2.adapter.MsgAdapter;
import com.example.section2.entity.Fruit;
import com.example.section2.entity.Msg;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity{
    //private List<Msg> msgList=new ArrayList<>();
    int msgSize;
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgViewModel msgViewModel;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msgRecyclerView=(RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        //msgRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));

        adapter=new MsgAdapter(new MsgAdapter.MsgDiff()); //instead of msglist
        msgRecyclerView.setAdapter(adapter);
        // Get a new or existing ViewModel from the ViewModelProvider.
        msgViewModel = new ViewModelProvider(this).get(MsgViewModel.class);

        //在getAllMsgs返回的LiveData上添加一个观察者。
        // onChanged()方法在观察到的数据发生变化时触发在前台。
        msgViewModel.getAllMsgs().observe(this,msgs->{
            adapter.submitList(msgs);
        });
        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText=(EditText) findViewById(R.id.input_text);
                String content =inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg=new Msg(content,Msg.TYPE_SENT);
                    msgViewModel.insert(msg); //如果msg的主键缺失，那么就insert就执行失败且不会报错，所以自增primiray id解决
                    //msgSize=msgViewModel.getAllMsgs().getValue().size();
                    //msgSize=msgViewModel.getSize();
                    //adapter.notifyItemRangeRemoved(0,msgSize-1);
                    //msgRecyclerView.scrollToPosition(msgSize-1);//ListView定位到最后一行
                    inputText.setText("");//清空输入框
                }
            }
        });
    }


}
