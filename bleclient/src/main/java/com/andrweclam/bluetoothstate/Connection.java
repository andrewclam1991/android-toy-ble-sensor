package com.andrweclam.bluetoothstate;

import android.bluetooth.BluetoothDevice;

import io.reactivex.Flowable;

public interface Connection {

  interface State{
    void setContext(Context context);
    void dropContext();
    Flowable<BluetoothDevice> discover();
    Flowable<Model> connect(String macAddress);
    Flowable<Model> disconnect();
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

  class Model{
    private String macAddress;
    private boolean isConnected;
    private boolean isCalibrated;

    public String getMacAddress() {
      return macAddress;
    }

    public void setMacAddress(String macAddress) {
      this.macAddress = macAddress;
    }

    public boolean isConnected() {
      return isConnected;
    }

    public void setConnected(boolean connected) {
      isConnected = connected;
    }

    public boolean isCalibrated() {
      return isCalibrated;
    }

    public void setCalibrated(boolean calibrated) {
      isCalibrated = calibrated;
    }
  }
}
