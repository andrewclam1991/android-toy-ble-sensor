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
 *
 * com.andrewclam.weatherclient.model.Peripheral
 */

package com.andrewclam.weatherclient.model;

import android.support.annotation.NonNull;

/**
 * Models a bluetooth peripheral device data
 */
public class Peripheral implements BaseModel {
  /**
   * UUID of the bluetooth peripheral device
   */
  private String uid;

  @Override
  public void setUid(@NonNull String uid) {
    this.uid = uid;
  }

  @NonNull
  @Override
  public String getUid() {
    return uid;
  }
}
