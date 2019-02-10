package com.andrweclam.bluetoothstate.platform;

import android.bluetooth.BluetoothDevice;

import com.andrweclam.bluetoothstate.Connection;
import com.andrweclam.bluetoothstate.exceptions.MissingLocationAdapterException;

import javax.annotation.Nullable;

import io.reactivex.Flowable;

public class MissingLocationAdapterState implements Connection.State {

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
    return Flowable.error(new MissingLocationAdapterException());
  }

  @Override
  public Flowable<Connection> connect() {
    return Flowable.error(new MissingLocationAdapterException());
  }

  @Override
  public Flowable<Connection> disconnect() {
    return Flowable.error(new MissingLocationAdapterException());
  }
}
