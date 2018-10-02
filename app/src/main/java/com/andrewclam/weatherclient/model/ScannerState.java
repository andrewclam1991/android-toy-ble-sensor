/*
 * Copyright 2018 Andrew Chi Heng Lam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andrewclam.weatherclient.model;

import android.support.annotation.NonNull;

/**
 * Models the current bluetooth adapter scanner state
 */
public class ScannerState implements BaseModel {
  private String stateId;
  private boolean isActive;

  private ScannerState() {

  }

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

  public static ScannerState getActiveState() {
    ScannerState activeState = new ScannerState();
    activeState.setActive(true);
    return activeState;
  }

  public static ScannerState getInActiveState() {
    ScannerState inactiveState = new ScannerState();
    inactiveState.setActive(false);
    return inactiveState;
  }
}
