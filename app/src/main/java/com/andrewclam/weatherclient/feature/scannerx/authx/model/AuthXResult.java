package com.andrewclam.weatherclient.feature.scannerx.authx.model;

import java.util.Objects;

/**
 * Models the result of a {@link AuthXCommand}
 */
public class AuthXResult {
  private final boolean isBluetoothLowEnergyAvailable;
  private final boolean isBluetoothAdapterAvailable;
  private final boolean isBluetoothPermissionGranted;

  public AuthXResult(boolean isBluetoothLowEnergyAvailable, boolean isBluetoothAdapterAvailable, boolean isBluetoothPermissionGranted) {
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
    return "AuthXResult{" +
        "isBluetoothLowEnergyAvailable=" + isBluetoothLowEnergyAvailable +
        ", isBluetoothAdapterAvailable=" + isBluetoothAdapterAvailable +
        ", isBluetoothPermissionGranted=" + isBluetoothPermissionGranted +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AuthXResult)) return false;
    AuthXResult authXResult = (AuthXResult) o;
    return isBluetoothLowEnergyAvailable() == authXResult.isBluetoothLowEnergyAvailable() &&
        isBluetoothAdapterAvailable() == authXResult.isBluetoothAdapterAvailable() &&
        isBluetoothPermissionGranted() == authXResult.isBluetoothPermissionGranted();
  }

  @Override
  public int hashCode() {
    return Objects.hash(isBluetoothLowEnergyAvailable(),
        isBluetoothAdapterAvailable(),
        isBluetoothPermissionGranted());
  }
}
