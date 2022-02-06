package com.sabithpkcmnr.filedownloader;

import android.app.Application;
import android.os.StrictMode;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

public class ActivityBase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        PRDownloader.initialize(getApplicationContext());
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);
    }
}
