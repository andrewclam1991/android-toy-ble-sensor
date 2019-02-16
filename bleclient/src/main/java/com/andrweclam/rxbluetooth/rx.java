package com.andrweclam.rxbluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

public class rx {

  @Inject
  Context mApplicationContext;

  private PublishProcessor<Data> mDataStream;

  private PublishProcessor<Model> mModelStream;

  private BluetoothGattCallback mGattCallback;

  private BluetoothGatt mGatt;

  public rx() {
    mGattCallback = new RxBluetoothGattCallback();
  }

  public Flowable<Model> connect(BluetoothDevice device) {
    mGatt = device.connectGatt(mApplicationContext, true, mGattCallback);
    return mModelStream;
  }

  // Generate for each field
  // @BLEcharacteristic(uuid = "12345644", type = int)
  public PublishProcessor<Model> get(){
    return mModelStream;
  }

  public Completable put(Model model){
    return Completable.complete();
  }

  public void disconnect() {
    mGatt.disconnect();
  }
}
