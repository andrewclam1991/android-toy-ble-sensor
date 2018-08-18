package com.andrewclam.toyblesensor.model;

import android.support.annotation.NonNull;

public interface BaseModel {
    void setUid(@NonNull String uid);

    @NonNull
    String getUid();
}
