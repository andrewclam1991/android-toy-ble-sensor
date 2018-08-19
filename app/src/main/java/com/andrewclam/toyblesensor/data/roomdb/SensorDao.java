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
 * com.andrewclam.toyblesensor.data.roomdb.SensorDao
 */

package com.andrewclam.toyblesensor.data.roomdb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.andrewclam.toyblesensor.model.Sensor;
import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface SensorDao extends BaseDao<Sensor> {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void add(Sensor sensor);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void addAll(List<Sensor> sensors);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void update(Sensor sensor);

  @Query("SELECT * from sensor ORDER BY uid ASC")
  Flowable<List<Sensor>> getAll();

  @Query("SELECT * FROM sensor WHERE uid == :sensorId LIMIT 1")
  Flowable<Optional<Sensor>> get(String sensorId);

  @Query("DELETE FROM sensor WHERE uid == :sensorId")
  void delete(String sensorId);

  @Query("DELETE FROM sensor")
  void deleteAll();
}
