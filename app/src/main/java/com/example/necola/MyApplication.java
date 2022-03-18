package com.example.necola;

import android.app.Application;

public class MyApplication extends Application {

    private String currentUsername;
    Boolean flag;
    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername= currentUsername;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getFlag() {
        return flag;
    }
}