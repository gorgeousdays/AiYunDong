package com.example.standardmotion.ui.postvideo;

public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}
