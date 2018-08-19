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
 * com.andrewclam.weatherclient.model.Sensor
 */

package com.andrewclam.weatherclient.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Models Sensor data
 */
@Entity(tableName = "sensor")
public class Sensor implements BaseModel {
  /**
   * Each Sensor's global uuid.
   */
  @NonNull
  @PrimaryKey
  @ColumnInfo(name = "uid")
  private String uid;

  /**
   * Sensor device's bluetooth mac address.
   */
  @ColumnInfo(name = "mac_address")
  private String macAddress;

  public Sensor(@NonNull String uid) {
    this.uid = uid;
  }

  @Override
  public void setUid(@NonNull String uid) {
    this.uid = uid;
  }

  @NonNull
  @Override
  public String getUid() {
    return uid;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }
}