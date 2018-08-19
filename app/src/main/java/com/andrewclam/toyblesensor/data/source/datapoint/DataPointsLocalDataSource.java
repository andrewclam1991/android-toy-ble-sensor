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
 * com.andrewclam.toyblesensor.data.source.datapoint.DataPointsLocalDataSource
 */

package com.andrewclam.toyblesensor.data.source.datapoint;

import android.support.annotation.NonNull;

import com.andrewclam.toyblesensor.data.roomdb.DataPointDao;
import com.andrewclam.toyblesensor.data.source.LocalDataSource;
import com.andrewclam.toyblesensor.model.DataPoint;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Local sqlite database implementation of {@link DataPointsDataSource}
 */
@Singleton
class DataPointsLocalDataSource extends LocalDataSource<DataPoint> implements DataPointsDataSource {

  @NonNull
  private final DataPointDao mDataPointDao;

  @Inject
  DataPointsLocalDataSource(@NonNull DataPointDao dataPointDao) {
    super(dataPointDao);
    this.mDataPointDao = dataPointDao;
  }

}
