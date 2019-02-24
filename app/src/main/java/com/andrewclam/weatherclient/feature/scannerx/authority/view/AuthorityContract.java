package com.andrewclam.weatherclient.feature.scannerx.authority.view;

import com.andrewclam.weatherclient.feature.scannerx.authority.model.AuthorityEvent;

import io.reactivex.Flowable;

public interface AuthorityContract {
  interface View {
    /**
     * Determines if the device has BLE feature
     *
     * @return
     */
    boolean hasBleFeature();

    /**
     * Determines if the device current has bluetooth adapter
     *
     * @return
     */
    boolean hasBTAdapter();

    /**
     * Determines if the application has the necessary permissions
     *
     * @return
     */
    boolean hasPermissions();
  }

  interface Controller {
    Flowable<AuthorityEvent> getEvent();
    void setHasBleFeature(boolean hasBleFeature);
    void setHasBtAdapter(boolean hasBtAdapter);
    void setHasPermissions(boolean hasPermissions);
  }
}
