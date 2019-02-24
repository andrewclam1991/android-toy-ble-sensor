package com.andrewclam.weatherclient.feature.scannerx.authx.data;

import com.andrewclam.weatherclient.feature.scannerx.authx.model.AuthX;

import io.reactivex.Flowable;

public interface AuthXDataSource {
  void setHasBluetoothLowEnergy(boolean hasBluetoothLowEnergy);
  void setHasBluetoothAdapter(boolean hasBluetoothAdapter);
  void setHasBluetoothPermission(boolean hasBluetoothPermission);

  Flowable<Boolean> getHasBluetoothLowEnergy();
  Flowable<Boolean> getHasBluetoothAdapter();
  Flowable<Boolean> getHasBluetoothPermission();

  Flowable<AuthX> getAuthX();
}
