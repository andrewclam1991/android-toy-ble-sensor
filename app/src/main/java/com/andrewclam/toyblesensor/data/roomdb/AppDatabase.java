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
 * com.andrewclam.toyblesensor.data.roomdb.AppDatabase
 */

package com.andrewclam.toyblesensor.data.roomdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.andrewclam.toyblesensor.model.DataPoint;
import com.andrewclam.toyblesensor.model.Sensor;
import com.andrewclam.toyblesensor.model.SensorDataPoint;

/**
 * Base class for Android Room to generate an App SQLite database.
 */
@Database(entities = {Sensor.class, DataPoint.class, SensorDataPoint.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  public abstract DataPointDao dataPointDao();

  public abstract SensorDao sensorDao();

  public abstract SensorDataPointDao sensorDataPointDao();
}
