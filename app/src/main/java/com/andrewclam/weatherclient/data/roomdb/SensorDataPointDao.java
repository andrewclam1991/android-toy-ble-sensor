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

package com.andrewclam.weatherclient.data.roomdb;

import com.andrewclam.weatherclient.model.DataPoint;
import com.andrewclam.weatherclient.model.SensorDataPoint;
import com.google.common.base.Optional;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface SensorDataPointDao extends BaseDao<SensorDataPoint> {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void add(SensorDataPoint pt);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void addAll(List<SensorDataPoint> pts);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void update(SensorDataPoint pt);

  @Query("SELECT * FROM sensor_data_point WHERE uid == :uid LIMIT 1")
  Flowable<Optional<SensorDataPoint>> get(String uid);

  @Query("SELECT * FROM sensor_data_point")
  Flowable<List<SensorDataPoint>> getAll();

  @Query("DELETE FROM sensor_data_point WHERE uid == :uid")
  void delete(String uid);

  @Query("DELETE FROM sensor_data_point")
  void deleteAll();

  @Query("SELECT * " +
      "FROM data_point " +
      "INNER JOIN sensor_data_point on sensor_data_point.data_point_id == data_point.uid " +
      "INNER JOIN sensor ON sensor_data_point.sensor_id == sensor.uid " +
      "WHERE sensor.uid LIKE :sensorId ")
  Flowable<List<DataPoint>> getDataPointsBySensorId(String sensorId);

  @Query("DELETE FROM sensor_data_point " +
      "WHERE sensor_data_point.sensor_id == :sensorId " +
      "AND sensor_data_point.data_point_id == :dataPointId")
  void delete(String sensorId, String dataPointId);

  @Query("DELETE FROM sensor_data_point " +
      "WHERE sensor_data_point.sensor_id == :sensorId ")
  void deleteAll(String sensorId);
}
