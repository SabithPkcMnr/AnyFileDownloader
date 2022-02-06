package com.sabithpkcmnr.filedownloader;

import android.content.Context;

import java.io.File;

public class ActivityConfig {

    public static String getAppFolder(Context context) {
        return getAppFolderForDownloadList(context) + File.separator;
    }

    public static String getAppFolderForDownloader(Context context) {
        return getAppFolderForDownloadList(context);
    }

    public static String getAppFolderForDownloadList(Context context) {
        return context.getExternalFilesDir("Downloads") + "";
    }


}
