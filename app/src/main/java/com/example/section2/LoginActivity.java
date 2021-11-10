package com.example.section2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity{
    private EditText aEdit;
    private EditText pEdit;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        aEdit=(EditText) findViewById(R.id.account);
        pEdit=(EditText) findViewById(R.id.password);
        login =(Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account=aEdit.getText().toString();
                String password=pEdit.getText().toString();

                if(account.equals("test") &&password.equals("test")){
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
