package com.andrewclam.weatherclient.feature.scannerx.authx.view;

import com.andrewclam.weatherclient.feature.scannerx.authx.model.AuthXCommand;

import io.reactivex.Flowable;

public interface AuthXContract {
  interface View {
    void isBluetoothLowEnergyAvailable();

    void isBluetoothAdapterAvailable();

    void isBluetoothPermissionGranted();

    void requestBluetoothAdapter();

    void requestBluetoothPermission();
  }

  interface Controller{
    Flowable<AuthXCommand> getCommand();
  }
}
