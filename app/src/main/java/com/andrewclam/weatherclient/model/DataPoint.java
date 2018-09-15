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
 * DataPoint.java
 *
 */

package com.andrewclam.weatherclient.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Models measurement data
 */
@Entity(tableName = "data_point")
public class DataPoint implements BaseModel {
  /**
   * Each measurement data point's global uuid.
   */
  @NonNull
  @PrimaryKey
  @ColumnInfo(name = "uid")
  private String uid;

  /**
   * remembers relative humidity,
   * valid range: 0 < rh
   */
  @ColumnInfo(name = "rh")
  private double rh;

  /**
   * remembers temperature data in Celsius
   */
  @ColumnInfo(name = "temp_c")
  private double tempC;

  /**
   * remembers temperature data in absolute Kelvin,
   * valid range: 0 < tempK
   */
  @ColumnInfo(name = "temp_k")
  private double tempK;

  /**
   * remembers this data point's collection unix time stamp
   */
  @ColumnInfo(name = "timestamp")
  private long timestamp;

  public DataPoint(@NonNull String uid) {
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

  public double getRh() {
    return rh;
  }

  public void setRh(double rh) {
    this.rh = rh;
  }

  public double getTempC() {
    return tempC;
  }

  public void setTempC(double tempC) {
    this.tempC = tempC;
  }

  public double getTempK() {
    return tempK;
  }

  public void setTempK(double tempK) {
    this.tempK = tempK;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
