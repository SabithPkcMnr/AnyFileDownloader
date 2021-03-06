package com.sabithpkcmnr.filedownloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.Locale;

public class ActivityHome extends AppCompatActivity {

    String fileLink;
    String fileName;

    ProgressBar pbProgress;
    EditText etName, etLink;
    MaterialButton btDownload;
    TextView txMbCurrent, txMbTotal, txStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etName = findViewById(R.id.etName);
        etLink = findViewById(R.id.etLink);
        txMbTotal = findViewById(R.id.txMbTotal);
        btDownload = findViewById(R.id.btDownload);
        txMbCurrent = findViewById(R.id.txMbCurrent);
        txStatus = findViewById(R.id.txDownloadStatus);
        pbProgress = findViewById(R.id.pbDownloadProgress);

        //Create App Folder To Save Files - Start
        File myFolder = new File(Environment.getExternalStorageDirectory() +
                File.separator + getResources().getString(R.string.app_name));
        if (!myFolder.exists()) {
            myFolder.mkdirs();
        }
        //Create App Folder To Save Files - End

        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileLink = etLink.getText().toString();
                fileName = etName.getText().toString();

                if (fileName.length() < 8 && !fileName.contains(".")) {
                    etName.setError("Enter valid file name!");

                } else if (fileLink.length() < 8) {
                    etLink.setError("Enter valid link!");

                } else {
                    downloadFile(fileLink, fileName);
                }
            }
        });

        //Let's ask for storage permission
        askStoragePermission();
    }

    private void askStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionArrays = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionArrays, 123);
        }
    }

    private void downloadFile(String fileLink, String fileName) {
        String downloadLocation = new File(Environment.getExternalStorageDirectory() +
                File.separator + getResources().getString(R.string.app_name)).getAbsolutePath();

        PRDownloader.download(fileLink, downloadLocation, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        btDownload.setEnabled(false);
                        txStatus.setText("Downloading...");
                        Log.d("logDownloadInfo", "Download Start/Resume");

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        btDownload.setEnabled(true);
                        txStatus.setText("Paused...");
                        Log.d("logDownloadInfo", "Download Paused");

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        btDownload.setEnabled(true);
                        txStatus.setText("Cancelled...");
                        Log.d("logDownloadInfo", "Download Cancelled");

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        txStatus.setText("Downloading...");
                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        pbProgress.setProgress((int) progressPercent);
                        txStatus.setText(progressPercent + "%");
                        txMbCurrent.setText(getBytesToMBString(progress.currentBytes));
                        txMbTotal.setText(getBytesToMBString(progress.totalBytes));
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        btDownload.setEnabled(true);
                        txStatus.setText("File Downloaded!");
                        Log.d("logDownloadInfo", "Download Complete");
                    }

                    @Override
                    public void onError(Error error) {
                        txStatus.setText("Error: " + error.getServerErrorMessage());
                        Log.d("logDownloadInfo", "Download Error: " + error.getServerErrorMessage());
                        Log.d("logDownloadInfo", "Download Error: " + error.getConnectionException());
                        Log.d("logDownloadInfo", "Download Error: " + error.getResponseCode());

                    }
                });
    }

    private String getBytesToMBString(long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMB", bytes / (1024.00 * 1024.00));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (requestCode == 123 && storagePermission == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted :)", Toast.LENGTH_SHORT).show();
            String currentPath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            final File newFolder = new File(currentPath.substring(0, currentPath.lastIndexOf("/")) +
                    File.separator + getResources().getString(R.string.app_name));
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askStoragePermission();
        }
    }

}