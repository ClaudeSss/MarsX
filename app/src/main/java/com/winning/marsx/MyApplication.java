package com.winning.marsx;

import android.app.Application;

import com.winning.marsx_security.core.DirectiveManager;

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        DirectiveManager.getInstance(this,"123abc");
    }
}
