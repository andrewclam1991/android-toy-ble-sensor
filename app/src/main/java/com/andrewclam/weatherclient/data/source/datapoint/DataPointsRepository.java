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
 * com.andrewclam.weatherclient.data.source.datapoint.DataPointsRepository
 */

package com.andrewclam.weatherclient.data.source.datapoint;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.andrewclam.weatherclient.data.source.Local;
import com.andrewclam.weatherclient.data.source.Remote;
import com.andrewclam.weatherclient.data.source.Repository;
import com.andrewclam.weatherclient.model.DataPoint;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Repository class responsible for managing cache,
 * local and remote types of {@link DataPointsDataSource}
 */

@Singleton
public class DataPointsRepository extends Repository<DataPoint> implements DataPointsDataSource {

  @NonNull
  private final DataPointsDataSource mLocalDataSource;

  @NonNull
  private final DataPointsDataSource mRemoteDataSource;

  @VisibleForTesting
  @Inject
  DataPointsRepository(@NonNull @Local DataPointsDataSource localDataSource,
                       @NonNull @Remote DataPointsDataSource remoteDataSource) {
    super(localDataSource, remoteDataSource);
    mLocalDataSource = checkNotNull(localDataSource);
    mRemoteDataSource = checkNotNull(remoteDataSource);
  }

}
