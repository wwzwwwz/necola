package com.example.necola;

import android.content.Context;
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

import androidx.annotation.NonNull;

import com.example.necola.service.background.MessageService;
import com.example.necola.utils.SocketIOClient;
import com.example.necola.utils.httpAPI.resource.Auth;

import java.io.IOException;

import io.socket.client.Socket;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends BaseActivity{
    private EditText aEdit;
    private EditText pEdit;
    private Button login;
    private CheckBox remPwsd;

    static public SharedPreferences pref;
    private SharedPreferences.Editor editor;

    static public void initLoadingLogin(Context context,String account){
        Intent intent=new Intent(context,FragmentTabActivity.class);
        intent.putExtra("currentUsername",account);

        context.startActivity(intent);
        Socket mSocket= SocketIOClient.getInstance();
        mSocket.connect();
        mSocket.emit("login",account);

        Intent messageIntent=new Intent(context, MessageService.class);
        messageIntent.putExtra("currentUsername",account);
        context.startService(messageIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        pref= PreferenceManager.getDefaultSharedPreferences(this);

        aEdit=(EditText) findViewById(R.id.account);
        pEdit=(EditText) findViewById(R.id.password);
        remPwsd=(CheckBox) findViewById(R.id.remeber_pswd);
        login =(Button) findViewById(R.id.login);
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

                Callback callback= new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response!=null)
                        {
                            if(response.code()<300)
                            {
                                ((MyApplication) getApplication()).setCurrentUsername(account);
                                editor=pref.edit();
                                editor.putBoolean("isSavedLogin",true);
                                if(remPwsd.isChecked()){
                                    editor.putBoolean("remember_password",true);
                                    editor.putString("account",account);
                                    editor.putString("password",password);

                                }
                                else{
                                    editor.clear();
                                }
                                editor.apply();
                                initLoadingLogin(LoginActivity.this,account);
                                LoginActivity.this.finish();
                            }
                            else
                                Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                            }


                    }
                };
                Auth.login(LoginActivity.this,callback,account,password);

            }
        }); 


    }
}
