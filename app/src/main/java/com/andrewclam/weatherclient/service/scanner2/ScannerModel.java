package com.andrewclam.weatherclient.service.scanner2;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.PublishProcessor;

class ScannerModel implements ScannerContract.Model {

  private PublishProcessor<BluetoothDevice> mDevicesPublisher;

  @NonNull
  private final BluetoothAdapter mAdapter;

  @Inject
  ScannerModel(@NonNull BluetoothAdapter adapter){
    mAdapter = adapter;
  }

  @Override
  public Flowable<BluetoothDevice> getDevices() {
    return mDevicesPublisher.onBackpressureBuffer(16);
  }

  @Override
  public Completable cancelGetDevices() {
    // TODO implement cancel subscription
    return Completable.complete();
  }
}
