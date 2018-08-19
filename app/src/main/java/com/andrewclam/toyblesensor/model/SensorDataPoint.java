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
 * com.andrewclam.toyblesensor.model.SensorDataPoint
 */

package com.andrewclam.toyblesensor.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Models the association between {@link Sensor} and its {@link DataPoint}s
 */
@Entity(
    primaryKeys = {"uid", "sensor_id", "data_point_id"},
    tableName = "sensor_data_point")
public class SensorDataPoint implements BaseModel {

  @NonNull
  @ColumnInfo(name = "uid")
  private String uid;

  @NonNull
  @ColumnInfo(name = "sensor_id")
  private String sensorId;

  @NonNull
  @ColumnInfo(name = "data_point_id")
  private String dataPointId;

  public SensorDataPoint(@NonNull String uid, @NonNull String sensorId, @NonNull String dataPointId) {
    this.uid = uid;
    this.sensorId = sensorId;
    this.dataPointId = dataPointId;
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

  @NonNull
  public String getSensorId() {
    return sensorId;
  }

  public void setSensorId(@NonNull String sensorId) {
    this.sensorId = sensorId;
  }

  @NonNull
  public String getDataPointId() {
    return dataPointId;
  }

  public void setDataPointId(@NonNull String dataPointId) {
    this.dataPointId = dataPointId;
  }
}
