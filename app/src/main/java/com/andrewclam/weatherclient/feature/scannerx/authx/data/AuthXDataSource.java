package com.andrewclam.weatherclient.feature.scannerx.authx.data;

import com.andrewclam.weatherclient.feature.scannerx.authx.model.AuthXResult;
import com.andrewclam.weatherclient.feature.scannerx.authx.model.AuthXCommand;

import io.reactivex.Flowable;

public interface AuthXDataSource {
  /**
   * Sets the device's current BLE capability status
   * @param hasBluetoothLowEnergy flags if this device supports BLE
   */
  void setHasBluetoothLowEnergy(boolean hasBluetoothLowEnergy);

  /**
   * Sets the device's current bluetooth adapter status
   * @param hasBluetoothAdapter flags if this device has bluetooth adapter
   */
  void setHasBluetoothAdapter(boolean hasBluetoothAdapter);

  /**
   * Sets the app current bluetooth permission status
   * @param hasBluetoothPermission flags if this app has bluetooth permission
   */
  void setHasBluetoothPermission(boolean hasBluetoothPermission);

  /**
   * Enqueues a {@link AuthXCommand}
   * @param command a {@link AuthXCommand}
   */
  void setAuthXCommand(AuthXCommand command);

  /**
   * Gets result whether this device supports BLE
   * @return true if this device supports BLE, false otherwise
   */
  Flowable<Boolean> getHasBluetoothLowEnergy();

  /**
   * Gets result whether this device's bluetooth adapter is available
   * @return true if it is available, false otherwise
   */
  Flowable<Boolean> getHasBluetoothAdapter();

  /**
   * Gets result whether this app has permission to use bluetooth adapter
   * @return true if permission is granted, false otherwise
   */
  Flowable<Boolean> getHasBluetoothPermission();

  /**
   * Gets a stream of {@link AuthXResult}
   * @return a {@link Flowable} stream of {@link AuthXResult}
   */
  Flowable<AuthXResult> getAuthXResult();

  /**
   * Gets a stream of {@link AuthXCommand}
   * @return a {@link Flowable} stream of {@link AuthXCommand}
   */
  Flowable<AuthXCommand> getAuthXCommand();
}
