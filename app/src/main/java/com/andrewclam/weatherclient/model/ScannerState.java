package com.andrewclam.weatherclient.model;

import android.support.annotation.NonNull;

/**
 * Models the current bluetooth adapter scanner state
 */
public class ScannerState implements BaseModel {
  private String stateId;
  private boolean isActive;

  public String getStateId() {
    return stateId;
  }

  public void setStateId(String stateId) {
    this.stateId = stateId;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  @Override
  public void setUid(@NonNull String uid) {
    stateId = uid;
  }

  @NonNull
  @Override
  public String getUid() {
    return stateId;
  }
}
