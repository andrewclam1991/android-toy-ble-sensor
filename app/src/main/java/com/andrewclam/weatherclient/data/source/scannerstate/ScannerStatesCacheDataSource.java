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

package com.andrewclam.weatherclient.data.source.scannerstate;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.model.ScannerState;
import com.google.common.base.Optional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import timber.log.Timber;

/**
 * Concrete implementation of a data source as a in memory cache
 */
@Singleton
class ScannerStatesCacheDataSource implements ScannerStatesDataSource {

  @VisibleForTesting
  @NonNull
  final Map<String, ScannerState> mCacheItems;

  @Inject
  ScannerStatesCacheDataSource() {
    mCacheItems = new LinkedHashMap<>();
  }

  @NonNull
  @Override
  public Flowable<Optional<ScannerState>> get(@NonNull String id) {
    final ScannerState cachedItem = mCacheItems.get(id);
    return Flowable.just(cachedItem != null ? Optional.of(cachedItem) : Optional.absent());
  }

  @NonNull
  @Override
  public Flowable<List<ScannerState>> getAll() {
    return Flowable.fromIterable(mCacheItems.values()).toList().toFlowable();
  }

  @NonNull
  @Override
  public Completable add(@NonNull ScannerState item) {
    return Completable.create(emitter -> {
      mCacheItems.put(item.getUid(), item);
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable add(@NonNull List<ScannerState> items) {
    return Completable.create(emitter -> {
      for (ScannerState item : items) {
        mCacheItems.put(item.getUid(), item);
      }
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable update(@NonNull ScannerState item) {
    return add(item);
  }

  @NonNull
  @Override
  public Completable delete(@NonNull String id) {
    return Completable.create(emitter -> {
      if (!mCacheItems.isEmpty() && mCacheItems.containsKey(id)) {
        mCacheItems.remove(id);
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
    mCacheItems.clear();
  }
}
