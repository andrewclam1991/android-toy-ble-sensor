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
 * SensorDataPointsDataSource.java
 *
 */

package com.andrewclam.weatherclient.data.source.sensordatapoint;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.data.source.DataSource;
import com.andrewclam.weatherclient.model.DataPoint;
import com.andrewclam.weatherclient.model.Sensor;
import com.andrewclam.weatherclient.model.SensorDataPoint;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * API exposes {@link SensorDataPoint} model specific data source requirements
 */
public interface SensorDataPointsDataSource extends DataSource<SensorDataPoint> {

  /* Allows for future extensibility */

  /**
   * Get a list of {@link DataPoint}s that are associated with a {@link Sensor}
   *
   * @param sensorId the unique id that identifies the {@link Sensor}
   * @return observable emission of {@link DataPoint}s that is associated with the
   * particular {@link Sensor} by id
   */
  @NonNull
  Flowable<List<DataPoint>> getDataPointsBySensorId(@NonNull String sensorId);

  /**
   * Deletes all {@link DataPoint}s that are associated with a {@link Sensor} by id
   *
   * @param sensorId the unique id that identifies the {@link Sensor}
   * @return observable completable event
   */
  @NonNull
  Completable deleteAll(@NonNull String sensorId);

}
