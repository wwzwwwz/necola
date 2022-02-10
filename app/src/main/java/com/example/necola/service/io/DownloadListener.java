package com.example.necola.service.io;

public interface DownloadListener  {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();
}
