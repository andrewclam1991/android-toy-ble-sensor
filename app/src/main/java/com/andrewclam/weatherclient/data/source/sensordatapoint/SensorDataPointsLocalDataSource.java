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
 * com.andrewclam.weatherclient.data.source.sensordatapoint.SensorDataPointsLocalDataSource
 */

package com.andrewclam.weatherclient.data.source.sensordatapoint;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.data.roomdb.SensorDataPointDao;
import com.andrewclam.weatherclient.data.source.LocalDataSource;
import com.andrewclam.weatherclient.model.DataPoint;
import com.andrewclam.weatherclient.model.SensorDataPoint;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;


/**
 * Concrete implementation of a data source as a db.
 */
@Singleton
class SensorDataPointsLocalDataSource extends LocalDataSource<SensorDataPoint> implements SensorDataPointsDataSource {

  @NonNull
  private final SensorDataPointDao mSensorDataPointDao;

  @Inject
  SensorDataPointsLocalDataSource(@NonNull SensorDataPointDao sensorDataPointDao) {
    super(sensorDataPointDao);
    this.mSensorDataPointDao = sensorDataPointDao;
  }

  @NonNull
  @Override
  public Flowable<List<DataPoint>> getDataPointsBySensorId(@NonNull String sensorId) {
    return mSensorDataPointDao.getDataPointsBySensorId(sensorId);
  }

  @NonNull
  @Override
  public Completable deleteAll(@NonNull String sensorId) {
    return Completable.create(emitter -> {
      mSensorDataPointDao.deleteAll(sensorId);
      emitter.onComplete();
    });
  }
}
