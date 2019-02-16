package com.andrweclam.bluetoothstate.service;

import android.bluetooth.BluetoothDevice;

import com.andrweclam.bluetoothstate.Connection;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class ServiceContext implements Connection.Context {
  private Connection.State mCurrentState;

  // Platform broadcast receiver
  @Inject
  Connection.State mMissingNetworkState;
  @Inject
  Connection.State mMissingBluetoothAdapterState;
  @Inject
  Connection.State mMissingLocationAdapterState;
  @Inject
  Connection.State mIdleState;

  public ServiceContext() {
    setContext(this);
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
    return mIdleState;
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
    mMissingNetworkState.setContext(context);
    mMissingBluetoothAdapterState.setContext(context);
    mMissingLocationAdapterState.setContext(context);
  }

  @Override
  public void dropContext() {
    mMissingNetworkState.dropContext();
    mMissingBluetoothAdapterState.dropContext();
    mMissingLocationAdapterState.dropContext();
  }

  @Override
  public Flowable<BluetoothDevice> discover() {
    return mCurrentState.discover();
  }

  @Override
  public Flowable<Connection.Model> connect(String macAddress) {
    return mCurrentState.connect(macAddress);
  }

  @Override
  public Flowable<Connection.Model> disconnect() {
    return mCurrentState.disconnect();
  }
}
