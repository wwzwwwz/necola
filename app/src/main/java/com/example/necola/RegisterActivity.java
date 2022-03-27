package com.example.necola;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.necola.utils.httpAPI.resource.Auth;

public class RegisterActivity extends BaseActivity {
    private EditText aEdit;
    private EditText pEdit,pEdit2;
    private EditText sexEdit;
    private String country_region;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        aEdit = (EditText) findViewById(R.id.username);
        pEdit = (EditText) findViewById(R.id.password);
        pEdit2=(EditText) findViewById(R.id.repeat_password);
        sexEdit=(EditText) findViewById(R.id.gender);
        register = (Button) findViewById(R.id.register_now);

        String regions[]=getResources().getStringArray(R.array.country_region_array);
        AutoCompleteTextView autodown=findViewById(R.id.autoCompleteTextView);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.item_country_region_dropdown,regions);
        autodown.setAdapter(arrayAdapter);
        autodown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country_region=(String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String username = aEdit.getText().toString();
                String password = pEdit.getText().toString();
                String password2=pEdit2.getText().toString();
                String sex=sexEdit.getText().toString();

                if (password.equals(password2)) {
                    if(Auth.register(RegisterActivity.this,username,password)) {

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        RegisterActivity.this.finish();
                        Toast.makeText(RegisterActivity.this, "Register Success! Please Login!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(RegisterActivity.this, "register failure,username has already exited!", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(RegisterActivity.this, "different password", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}

