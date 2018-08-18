package com.andrewclam.toyblesensor.service;

import android.support.annotation.NonNull;

public interface BaseController<S extends BaseService> {
    void setService(@NonNull S service);
    void dropService();
}
