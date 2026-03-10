package org.bkl.game;

public interface LauncherCallback {
    void onProgress(String message, double progress);
    void onSuccess();
    void onError(String errorMessage);
    void onLog(String logMessage);
}
