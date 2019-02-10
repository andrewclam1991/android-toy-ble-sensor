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

package com.andrewclam.weatherclient.data.source.sensordatapoint;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.andrewclam.weatherclient.data.source.Local;
import com.andrewclam.weatherclient.data.source.Remote;
import com.andrewclam.weatherclient.data.source.Repository;
import com.andrewclam.weatherclient.data.source.sensor.SensorsDataSource;
import com.andrewclam.weatherclient.model.DataPoint;
import com.andrewclam.weatherclient.model.SensorDataPoint;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Repository class responsible for managing cache,
 * local and remote types of {@link SensorsDataSource}
 */

@Singleton
public class SensorDataPointRepository extends Repository<SensorDataPoint> implements SensorDataPointDataSource {

  @NonNull
  private final SensorDataPointDataSource mLocalDataSource;

  @NonNull
  private final SensorDataPointDataSource mRemoteDataSource;

  @VisibleForTesting
  @Inject
  SensorDataPointRepository(@NonNull @Local SensorDataPointDataSource localDataSource,
                            @NonNull @Remote SensorDataPointDataSource remoteDataSource) {
    super(localDataSource, remoteDataSource);
    mLocalDataSource = checkNotNull(localDataSource);
    mRemoteDataSource = checkNotNull(remoteDataSource);
  }

  @NonNull
  @Override
  public Flowable<List<DataPoint>> getDataPointsBySensorId(@NonNull String sensorId) {
    return mLocalDataSource.getDataPointsBySensorId(sensorId)
        .switchIfEmpty(mRemoteDataSource.getDataPointsBySensorId(sensorId));
  }

  @NonNull
  @Override
  public Completable deleteAll(@NonNull String sensorId) {
    return mLocalDataSource.deleteAll(sensorId).andThen(mRemoteDataSource.deleteAll(sensorId));
  }
}
