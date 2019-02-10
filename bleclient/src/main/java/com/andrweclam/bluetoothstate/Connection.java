package com.andrweclam.bluetoothstate;

import android.bluetooth.BluetoothDevice;

import io.reactivex.Flowable;

public interface Connection {
  interface State{
    void setContext(Context context);
    void dropContext();
    Flowable<BluetoothDevice> discover();
    Flowable<Connection> connect();
    Flowable<Connection> disconnect();
  }

  interface Context extends State{
    State getMissingBluetoothAdapterState();
    State getMissingLocationAdapterState();
    State getMissingLocationPermissionState();
    State getMissingNetworkState();
    State getIdleState();
    State getDiscoveringState();
    State getConnectingState();
    State getConnectedState();
    State getDisconnectingState();
    State getDisconnectedState();
    void setState(State state);
  }
}
