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

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.andrewclam.weatherclient.model.Peripheral;
import com.google.common.base.Optional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Concrete implementation of a data source as a in memory cache
 */
@Singleton
class PeripheralsCacheDataSource implements PeripheralsDataSource {

  @NonNull
  private final BluetoothAdapter mBluetoothAdapter;

  @NonNull
  private AtomicBoolean mIsScanning;

  @VisibleForTesting
  @NonNull
  final Map<String, Peripheral> mCachePeripherals;

  // Stops scanning after 10 seconds
  private static final long SCAN_PERIOD_SECONDS = 10;

  @Inject
  PeripheralsCacheDataSource(@NonNull BluetoothAdapter bluetoothAdapter) {
    mBluetoothAdapter = bluetoothAdapter;
    mIsScanning = new AtomicBoolean(false);
    mCachePeripherals = new LinkedHashMap<>();
  }

  @NonNull
  @Override
  public Flowable<List<Peripheral>> scan() {
    // Get scanned result, then add to cache, then return all result after scan period
    return getPeripheral()
        .flatMapCompletable(this::add)
        .delay(SCAN_PERIOD_SECONDS, TimeUnit.SECONDS)
        .andThen(getAll())
        .doOnSubscribe(subscription -> mIsScanning.set(true))
        .doOnTerminate(() -> {
          // TODO andrew investigate creating immutable callback that can be reused for releasing scanner
          mBluetoothAdapter.stopLeScan(null);
          mIsScanning.set(false);
        });
  }

  private Flowable<Peripheral> getPeripheral() {
    return Flowable.create(emitter ->
        mBluetoothAdapter.startLeScan((device, rssi, scanRecord) -> {
          Peripheral peripheral = new Peripheral();
          peripheral.setBluetoothDevice(checkNotNull(device, "device can't be null"));
          peripheral.setUid(device.getAddress());
          emitter.onNext(peripheral);
        }), BackpressureStrategy.BUFFER);
  }

  @Override
  public boolean isScanning() {
    return mIsScanning.get();
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
