package com.group6.choul.login_register_handling;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
//        if(LeakCanary.isInAnalyzerProcess(this)){
//
//            return;
//        }
//        LeakCanary.install(this);
    }
}
