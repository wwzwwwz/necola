package com.example.necola;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.necola.service.MessageService;
import com.example.necola.utils.SocketIOClient;
import com.example.necola.utils.httpAPI.resource.Auth;


public class LoginActivity extends BaseActivity{
    private EditText aEdit;
    private EditText pEdit;
    private Button login,register;
    private CheckBox remPwsd;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        pref= PreferenceManager.getDefaultSharedPreferences(this);

        aEdit=(EditText) findViewById(R.id.account);
        pEdit=(EditText) findViewById(R.id.password);
        remPwsd=(CheckBox) findViewById(R.id.remeber_pswd);
        login =(Button) findViewById(R.id.login);
        register=(Button) findViewById(R.id.register);
        boolean isRem=pref.getBoolean("remember_password",false);
        if (isRem){
            String account=pref.getString("account","");
            String password =pref.getString("password","");
            aEdit.setText(account);
            pEdit.setText(password);
            remPwsd.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account=aEdit.getText().toString();
                String password=pEdit.getText().toString();

                if(Auth.login(account,password)){
                    editor=pref.edit();
                    if(remPwsd.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",password);

                    }
                    else{
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent=new Intent(LoginActivity.this,FragmentTabActivity.class);
                    intent.putExtra("currentUsername",account);
                    startActivity(intent);
                    mSocket= SocketIOClient.getInstance();
                    mSocket.connect();
                    mSocket.emit("login",account);
                    currentUsername=account;
                    Intent messageIntent=new Intent(LoginActivity.this, MessageService.class);
                    messageIntent.putExtra("currentUsername",currentUsername);
                    startService(messageIntent);
                    LoginActivity.this.finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();

                }
            }
        }); 


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
