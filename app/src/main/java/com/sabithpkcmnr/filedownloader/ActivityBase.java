package com.sabithpkcmnr.filedownloader;

import android.app.Application;

import com.downloader.PRDownloader;

public class ActivityBase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PRDownloader.initialize(getApplicationContext());
    }
}
