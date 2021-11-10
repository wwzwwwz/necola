package com.example.section2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends BaseActivity {

    ActivityResultLauncher<Intent> launchSomeActivity =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent intent=result.getData();;
                        String data=intent.getStringExtra("data_return");
                        Log.d("FirstActivity",data);
                    }
                }
            }
    );

    public void openSomeActivityForResult() {
        String data="Hello SecondA~";
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("extra_data",data);
        launchSomeActivity.launch(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Log.d("FirstActivity", "Task id is " + getTaskId());
        setContentView(R.layout.first_layout);
        Button button1=(Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                actionStart(FirstActivity.this,SecondActivity.class,"data1","data2");
                //Intent intent =new Intent(FirstActivity.this,FirstActivity.class);
                //Intent intent=new Intent("com.example.section2.ACTION_START");
                //SecondActivity.actionStart(FirstActivity.this, "data1", "data2");
                //openSomeActivityForResult();
                //Intent intent =new Intent(FirstActivity.this,SecondActivity.class);
                //Intent intent=new Intent("com.example.section2.ACTION_START");

                //startActivity(intent);
                //startActivityForResult(intent,1); 'startActivityForResult(android.content.Intent, int)' is deprecated

            }
        });
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("FirstActivity","Task onRestart");
    }


}