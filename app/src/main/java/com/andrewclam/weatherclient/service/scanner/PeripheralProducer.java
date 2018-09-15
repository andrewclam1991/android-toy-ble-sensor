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
 * PeripheralProducer.java
 *
 */

package com.andrewclam.weatherclient.service.scanner;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.model.Peripheral;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import timber.log.Timber;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Framework level peripheral producer, handles just start and stop production
 * abstract to separate framework logic details from core business logic
 */
@ServiceScoped
final class PeripheralProducer implements ScannerContract.Producer {

  @Nonnull
  private final BluetoothAdapter mBluetoothAdapter;

  @Nonnull
  private final BluetoothAdapter.LeScanCallback mLeScanCallback;

  @Nonnull
  private final PublishProcessor<Peripheral> mPeripheralSource;

  @Inject
  PeripheralProducer(@Nonnull BluetoothAdapter bluetoothAdapter) {
    mBluetoothAdapter = bluetoothAdapter;
    mLeScanCallback = getLeCallback();
    mPeripheralSource = PublishProcessor.create();
  }

  @NonNull
  @Override
  public Flowable<Peripheral> start() {
    return mPeripheralSource.doOnSubscribe(subscription ->
        mBluetoothAdapter.startLeScan(mLeScanCallback));
  }

  @NonNull
  @Override
  public Completable stop() {
    return Completable.create(emitter -> {
      mBluetoothAdapter.stopLeScan(mLeScanCallback);
      emitter.onComplete();
    });
  }

  /**
   * @return instance of reactive {@link BluetoothAdapter.LeScanCallback}
   */
  @NonNull
  private BluetoothAdapter.LeScanCallback getLeCallback() {
    return (device, rssi, scanRecord) -> {
      Peripheral peripheral = new Peripheral();
      peripheral.setBluetoothDevice(checkNotNull(device, "device can't be null"));
      peripheral.setRssi(rssi);
      peripheral.setScanRecord(scanRecord);
      peripheral.setUid(device.getAddress());
      mPeripheralSource.onNext(peripheral);
      Timber.d("got Peripheral: %s", peripheral.getUid());
    };
  }
}
