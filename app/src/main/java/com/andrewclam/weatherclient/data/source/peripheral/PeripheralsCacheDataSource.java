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
 * PeripheralsCacheDataSource.java
 *
 */

package com.andrewclam.weatherclient.data.source.peripheral;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.model.Peripheral;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import timber.log.Timber;

/**
 * Concrete implementation of a data source as a in memory cache
 * - Whenever a list is updated, notify the subscribers
 */
@Singleton
class PeripheralsCacheDataSource implements PeripheralsDataSource {

  @NonNull
  private final List<Peripheral> mCachePeripherals;

  @Nonnull
  private final PublishProcessor<List<Peripheral>> mCachePeripheralsPublisher;

  @Inject
  PeripheralsCacheDataSource() {
    mCachePeripherals = new ArrayList<>();
    mCachePeripheralsPublisher = PublishProcessor.create();
  }

  @NonNull
  @Override
  public Flowable<Optional<Peripheral>> get(@NonNull String id) {
    Timber.w("Data source doesn't support get by id");
    return Flowable.empty();
  }

  @NonNull
  @Override
  public Flowable<List<Peripheral>> getAll() {
    return mCachePeripheralsPublisher;
  }

  @NonNull
  @Override
  public Completable add(@NonNull Peripheral item) {
    return Completable.create(emitter -> {
      Timber.d("Add item: %s", item.getUid());
      if (!mCachePeripherals.contains(item)) {
        mCachePeripherals.add(item);
        mCachePeripheralsPublisher.onNext(mCachePeripherals);
      }
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable add(@NonNull List<Peripheral> items) {
    return Completable.create(emitter -> {
      mCachePeripherals.addAll(items);
      mCachePeripheralsPublisher.onNext(mCachePeripherals);
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable update(@NonNull Peripheral item) {
    return add(item);
  }

  @NonNull
  @Override
  public Completable delete(@NonNull String id) {
    Timber.w("Data source doesn't support delete by id");
    return Completable.complete();
  }

  @NonNull
  @Override
  public Completable deleteAll() {
    return Completable.create(emitter -> {
      refresh();
      mCachePeripheralsPublisher.onNext(mCachePeripherals);
      emitter.onComplete();
    });
  }

  @Override
  public void refresh() {
    mCachePeripherals.clear();
  }
}
