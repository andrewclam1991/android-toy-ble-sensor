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

package com.andrewclam.weatherclient.data.source;

import com.andrewclam.weatherclient.data.roomdb.BaseDao;
import com.andrewclam.weatherclient.model.BaseModel;
import com.google.common.base.Optional;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Abstraction of Local CRUD operations
 *
 * @param <E> type of {@link BaseModel}
 */
public abstract class LocalDataSource<E extends BaseModel> implements DataSource<E> {

  @NonNull
  private final BaseDao<E> mDao;

  protected LocalDataSource(@NonNull BaseDao<E> dao) {
    mDao = dao;
  }

  @Override
  public void refresh() { /* no implementation */ }

  @NonNull
  @Override
  public Completable add(@NonNull E item) {
    return Completable.create(emitter -> {
      mDao.add(item);
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable add(@NonNull List<E> items) {
    return Completable.create(emitter -> {
      mDao.addAll(items);
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Flowable<List<E>> getAll() {
    return mDao.getAll();
  }

  @NonNull
  @Override
  public Flowable<Optional<E>> get(@NonNull String entityId) {
    return mDao.get(entityId);
  }

  @NonNull
  @Override
  public Completable update(@NonNull E item) {
    return Completable.create(emitter -> {
      mDao.update(item);
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable delete(@NonNull String entityId) {
    return Completable.create(emitter -> {
      mDao.delete(entityId);
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable deleteAll() {
    return Completable.create(emitter -> {
      mDao.deleteAll();
      emitter.onComplete();
    });
  }
}
