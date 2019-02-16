package com.andrweclam.bluetoothstate.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import com.andrweclam.bluetoothstate.Connection;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public class IdleState implements Connection.State {

  private Connection.Context mContext;

  @NonNull
  private final Context mApplicationContext;

  @Inject
  public IdleState(@NonNull Context applicationContext){
    mApplicationContext = applicationContext;
  }

  @Override
  public void setContext(Connection.Context context) {
    mContext = context;
  }

  @Override
  public void dropContext() {
    mContext = null;
  }

  @Override
  public Flowable<BluetoothDevice> discover() {
    mContext.setState(mContext.getDiscoveringState());
    return mContext.discover();
  }

  @Override
  public Flowable<Connection.Model> connect(String macAddress) {
    return Flowable.create(emitter -> {
    }, BackpressureStrategy.LATEST);
  }

  @Override
  public Flowable<Connection.Model> disconnect() {
    return Flowable.error(new UnsupportedOperationException("Disconnecting in no device state is non-sense"));
  }
}
