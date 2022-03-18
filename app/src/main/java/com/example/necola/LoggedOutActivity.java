package com.example.necola;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.necola.utils.httpAPI.resource.Auth;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoggedOutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_surface);
        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("isSavedLogin",false)){
            currentUsername=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("account","");
            String password=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("password","");
            Callback callback=new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response==null) {
                        //new AlertDialog(LoggedOutActivity.this).setTitle("Error").setMessage("Network exception").create().show();
                    }
                    else{
                        int code = response.code();
                        if(code<300){
                            ((MyApplication) getApplication()).setCurrentUsername(currentUsername);
                            LoginActivity.initLoadingLogin(LoggedOutActivity.this,currentUsername);
                            LoggedOutActivity.this.finish();
                        }
                    }



                }
            };
            Auth.login(this,callback,currentUsername,password);





        }
        else{
            Button login=findViewById(R.id.surface_login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoggedOutActivity.this,LoginActivity.class));
                }
            });

            Button register=findViewById(R.id.surface_register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoggedOutActivity.this,RegisterActivity.class));
                    LoggedOutActivity.this.finish();
                }
            });

        }



    }

}