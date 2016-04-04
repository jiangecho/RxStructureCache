package com.echo.rxstructurecache;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by jiangecho on 16/4/4.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
