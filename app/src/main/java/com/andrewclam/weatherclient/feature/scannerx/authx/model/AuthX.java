package com.andrewclam.weatherclient.feature.scannerx.authx.model;

import java.util.Objects;

public class AuthX {
  private final boolean isBluetoothLowEnergyAvailable;
  private final boolean isBluetoothAdapterAvailable;
  private final boolean isBluetoothPermissionGranted;

  public AuthX(boolean isBluetoothLowEnergyAvailable, boolean isBluetoothAdapterAvailable, boolean isBluetoothPermissionGranted) {
    this.isBluetoothLowEnergyAvailable = isBluetoothLowEnergyAvailable;
    this.isBluetoothAdapterAvailable = isBluetoothAdapterAvailable;
    this.isBluetoothPermissionGranted = isBluetoothPermissionGranted;
  }

  public boolean isBluetoothLowEnergyAvailable() {
    return isBluetoothLowEnergyAvailable;
  }

  public boolean isBluetoothAdapterAvailable() {
    return isBluetoothAdapterAvailable;
  }

  public boolean isBluetoothPermissionGranted() {
    return isBluetoothPermissionGranted;
  }

  public boolean isAuthorized(){
    return isBluetoothLowEnergyAvailable && isBluetoothAdapterAvailable && isBluetoothPermissionGranted;
  }

  public boolean isDenied(){
    return !isBluetoothLowEnergyAvailable || !isBluetoothAdapterAvailable || !isBluetoothPermissionGranted;
  }

  @Override
  public String toString() {
    return "AuthX{" +
        "isBluetoothLowEnergyAvailable=" + isBluetoothLowEnergyAvailable +
        ", isBluetoothAdapterAvailable=" + isBluetoothAdapterAvailable +
        ", isBluetoothPermissionGranted=" + isBluetoothPermissionGranted +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AuthX)) return false;
    AuthX authX = (AuthX) o;
    return isBluetoothLowEnergyAvailable() == authX.isBluetoothLowEnergyAvailable() &&
        isBluetoothAdapterAvailable() == authX.isBluetoothAdapterAvailable() &&
        isBluetoothPermissionGranted() == authX.isBluetoothPermissionGranted();
  }

  @Override
  public int hashCode() {
    return Objects.hash(isBluetoothLowEnergyAvailable(),
        isBluetoothAdapterAvailable(),
        isBluetoothPermissionGranted());
  }
}
