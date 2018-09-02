package com.andrewclam.weatherclient.service.scanner;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.model.Peripheral;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
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
  private final PublishProcessor<Peripheral> mPeripheralSource = PublishProcessor.create();

  @Inject
  PeripheralProducer(@Nonnull BluetoothAdapter bluetoothAdapter) {
    mBluetoothAdapter = bluetoothAdapter;
    mLeScanCallback = getLeCallback();
  }

  @NonNull
  @Override
  public Flowable<Peripheral> start() {
    mBluetoothAdapter.startLeScan(mLeScanCallback);
    return mPeripheralSource;
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
