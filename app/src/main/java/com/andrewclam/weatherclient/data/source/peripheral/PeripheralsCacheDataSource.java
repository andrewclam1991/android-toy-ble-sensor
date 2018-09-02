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
 * com.andrewclam.weatherclient.data.source.peripheral.PeripheralsCacheDataSource
 */

package com.andrewclam.weatherclient.data.source.peripheral;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.andrewclam.weatherclient.model.Peripheral;
import com.google.common.base.Optional;

import java.io.PushbackInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

  @VisibleForTesting
  @NonNull
  final Map<String, Peripheral> mCachePeripherals;

  @Nonnull
  private final PublishProcessor<Map<String, Peripheral>> mCachePeripheralsPublisher;

  @Inject
  PeripheralsCacheDataSource() {
    mCachePeripherals = new LinkedHashMap<>();
    mCachePeripheralsPublisher = PublishProcessor.create();
  }

  @NonNull
  @Override
  public Flowable<Optional<Peripheral>> get(@NonNull String id) {
    final Peripheral cachedItem = mCachePeripherals.get(id);
    return Flowable.just(cachedItem != null ? Optional.of(cachedItem) : Optional.absent());
  }

  @NonNull
  @Override
  public Flowable<List<Peripheral>> getAll() {
    return Flowable.fromIterable(mCachePeripherals.values()).toList().toFlowable();
  }

  @NonNull
  @Override
  public Completable add(@NonNull Peripheral item) {
    return Completable.create(emitter -> {
      mCachePeripherals.put(item.getUid(), item);
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable add(@NonNull List<Peripheral> items) {
    return Completable.create(emitter -> {
      for (Peripheral item : items) {
        mCachePeripherals.put(item.getUid(), item);
      }
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
    return Completable.create(emitter -> {
      if (!mCachePeripherals.isEmpty() && mCachePeripherals.containsKey(id)) {
        mCachePeripherals.remove(id);
        Timber.d("Cached peripheral removed: %s", id);
      } else {
        Timber.d("Cache peripherals doesn't contain item with id: %s", id);
      }
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable deleteAll() {
    return Completable.create(emitter -> {
      refresh();
      emitter.onComplete();
    });
  }

  @Override
  public void refresh() {
    mCachePeripherals.clear();
  }
}
