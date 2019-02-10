package com.andrweclam.bluetoothstate.service;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.andrweclam.bluetoothstate.Connection;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.Flowable;

public class ServiceContext extends DaggerService implements Connection.Context {
  private Connection.State mCurrentState;

  // Platform broadcast receiver
  @Inject
  Connection.State mMissingNetworkState;
  @Inject
  Connection.State mMissingBluetoothAdapterState;
  @Inject
  Connection.State mMissingLocationAdapterState;

  public ServiceContext() {

  }

  @Override
  public void onCreate() {
    super.onCreate();
    setContext(this);
    mMissingNetworkState.setContext(this);
    mMissingBluetoothAdapterState.setContext(this);
    mMissingLocationAdapterState.setContext(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    dropContext();
    mMissingNetworkState.dropContext();
    mMissingBluetoothAdapterState.dropContext();
    mMissingLocationAdapterState.dropContext();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Connection.State getMissingBluetoothAdapterState() {
    return mMissingBluetoothAdapterState;
  }

  @Override
  public Connection.State getMissingLocationAdapterState() {
    return mMissingLocationAdapterState;
  }

  @Override
  public Connection.State getMissingLocationPermissionState() {
    return mMissingLocationAdapterState;
  }

  @Override
  public Connection.State getMissingNetworkState() {
    return mMissingNetworkState;
  }

  @Override
  public Connection.State getIdleState() {
    return null;
  }

  @Override
  public Connection.State getConnectingState() {
    return null;
  }

  @Override
  public Connection.State getConnectedState() {
    return null;
  }

  @Override
  public Connection.State getDisconnectingState() {
    return null;
  }

  @Override
  public Connection.State getDisconnectedState() {
    return null;
  }

  @Override
  public Connection.State getDiscoveringState() {
    return null;
  }

  @Override
  public void setState(Connection.State state) {
    mCurrentState = state;
  }

  @Override
  public void setContext(Connection.Context context) {
    // No implementation, this is the context implementation
  }

  @Override
  public void dropContext() {
    // No implementation, this is the context implementation
  }

  @Override
  public Flowable<BluetoothDevice> discover() {
    return mCurrentState.discover();
  }

  @Override
  public Flowable<Connection> connect() {
    return mCurrentState.connect();
  }

  @Override
  public Flowable<Connection> disconnect() {
    return mCurrentState.disconnect();
  }
}
