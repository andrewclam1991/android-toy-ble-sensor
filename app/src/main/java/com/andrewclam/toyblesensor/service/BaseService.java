package com.andrewclam.toyblesensor.service;

import android.support.annotation.NonNull;

import com.andrewclam.toyblesensor.view.BaseView;

public interface BaseService<V extends BaseView> {
    void addView(@NonNull V view, @NonNull String viewTag);
    void dropView(@NonNull String viewTag);
    void startService();
    void stopService();
}
