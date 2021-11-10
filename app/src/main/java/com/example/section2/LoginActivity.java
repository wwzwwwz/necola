package com.example.section2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity{
    private EditText aEdit;
    private EditText pEdit;
    private Button login;
    private CheckBox remPwsd;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

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

                if(account.equals("test") &&password.equals("test")){
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
                    //Intent intent=new Intent(LoginActivity.this,FragmentTabActivity.class);
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
