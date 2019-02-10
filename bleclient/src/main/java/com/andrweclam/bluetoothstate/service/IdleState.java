package com.andrweclam.bluetoothstate.service;

import android.bluetooth.BluetoothDevice;

import com.andrweclam.bluetoothstate.Connection;

import io.reactivex.Flowable;

public class IdleState implements Connection.State {

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
    mContext.setState(mContext.getDiscoveringState());
    return mContext.discover();
  }

  @Override
  public Flowable<Connection> connect() {
    mContext.setState(mContext.getConnectingState());
    return mContext.connect();
  }

  @Override
  public Flowable<Connection> disconnect() {
    return Flowable.error(new UnsupportedOperationException("Disconnect in idle state is non-sense"));
  }
}
