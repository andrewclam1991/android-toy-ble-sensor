package com.andrewclam.weatherclient.service.scanner;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.model.Peripheral;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

/**
 * Framework level peripheral producer, handles just start and stop production
 * abstract to separate framework logic details from core business logic
 */
@Singleton
final class PeripheralProducer implements ScannerContract.Producer {

  @NonNull
  private final BluetoothAdapter mBluetoothAdapter;

  @Nullable
  private BluetoothAdapter.LeScanCallback mLeScanCallback;

  @Inject
  PeripheralProducer(@NonNull BluetoothAdapter bluetoothAdapter) {
    mBluetoothAdapter = bluetoothAdapter;
  }

  @NonNull
  @Override
  public Flowable<Peripheral> start() {
    return Flowable.create(emitter -> mBluetoothAdapter.startLeScan(getLeCallback(emitter)),
        BackpressureStrategy.DROP);
  }

  @NonNull
  @Override
  public Completable stop() {
    return Completable.create(emitter -> {
      if (mLeScanCallback != null) {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        emitter.onComplete();
      } else {
        emitter.onError(new IllegalArgumentException("producer's scanner callback is null"));
      }
    });
  }

  /**
   * Generates a reactive BluetoothAdapter.LeScanCallback to be used in
   * {@link BluetoothAdapter#startLeScan(BluetoothAdapter.LeScanCallback)}
   *
   * @param emitter instance of {@link FlowableEmitter<Peripheral>} source
   * @return instance of reactive {@link BluetoothAdapter.LeScanCallback}
   */
  @NonNull
  private BluetoothAdapter.LeScanCallback getLeCallback(FlowableEmitter<Peripheral> emitter) {
    if (mLeScanCallback == null) {
      mLeScanCallback = (device, rssi, scanRecord) -> {
        Peripheral peripheral = new Peripheral();
        peripheral.setBluetoothDevice(checkNotNull(device, "device can't be null"));
        peripheral.setRssi(rssi);
        peripheral.setScanRecord(scanRecord);
        peripheral.setUid(device.getAddress());
        emitter.onNext(peripheral);
      };
    }
    return mLeScanCallback;
  }
}
