package com.example.section2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends BaseActivity {

    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("SecondActivity", "Task id is " + getTaskId());
        setContentView(R.layout.activity_second);
        //Intent intent=getIntent();
        //String data=intent.getStringExtra("extra_data");
        //Log.d("SecondActivity",data);
        Button button2 =(Button) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Intent intent=new Intent();
                //intent.putExtra("data_return","Hello First!");
                //setResult(RESULT_OK,intent);
                Intent intent=new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("SecondActivity","Task onDestroy");
    }



}