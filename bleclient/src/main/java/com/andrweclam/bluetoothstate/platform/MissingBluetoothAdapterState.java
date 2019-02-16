package com.andrweclam.bluetoothstate.platform;

import android.bluetooth.BluetoothDevice;

import com.andrweclam.bluetoothstate.Connection;
import com.andrweclam.bluetoothstate.exceptions.MissingBluetoothAdapterException;

import javax.annotation.Nullable;

import io.reactivex.Flowable;

public class MissingBluetoothAdapterState implements Connection.State {

  @Nullable
  private Connection.Context mContext;

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
    return Flowable.error(new MissingBluetoothAdapterException());
  }

  @Override
  public Flowable<Connection.Model> connect(String macAddress) {
    return Flowable.error(new MissingBluetoothAdapterException());
  }

  @Override
  public Flowable<Connection.Model> disconnect() {
    return Flowable.error(new MissingBluetoothAdapterException());
  }
}
