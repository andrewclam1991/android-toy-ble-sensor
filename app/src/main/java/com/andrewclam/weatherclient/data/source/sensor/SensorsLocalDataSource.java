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

package com.andrewclam.weatherclient.data.source.sensor;

import com.andrewclam.weatherclient.data.roomdb.SensorDao;
import com.andrewclam.weatherclient.data.source.LocalDataSource;
import com.andrewclam.weatherclient.model.Sensor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;


/**
 * Concrete implementation of a data source as a db.
 */
@Singleton
class SensorsLocalDataSource extends LocalDataSource<Sensor> implements SensorsDataSource {

  @NonNull
  private final SensorDao mSensorDao;

  @Inject
  SensorsLocalDataSource(@NonNull SensorDao sensorDao) {
    super(sensorDao);
    this.mSensorDao = sensorDao;
  }

}
