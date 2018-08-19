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
 * com.andrewclam.toyblesensor.data.roomdb.DataPointDao
 */

package com.andrewclam.toyblesensor.data.roomdb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.andrewclam.toyblesensor.model.DataPoint;
import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DataPointDao extends BaseDao<DataPoint> {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void add(DataPoint pt);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void addAll(List<DataPoint> pts);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void update(DataPoint pt);

  @Query("SELECT * from data_point ORDER BY timestamp ASC")
  Flowable<List<DataPoint>> getAll();

  @Query("SELECT * FROM data_point WHERE uid == :uid LIMIT 1")
  Flowable<Optional<DataPoint>> get(String uid);

  @Query("DELETE FROM data_point WHERE uid == :uid")
  void delete(String uid);

  @Query("DELETE FROM data_point")
  void deleteAll();
}
