package com.andrewclam.weatherclient.feature.scanner.model;

import android.bluetooth.BluetoothDevice;

/**
 * Model for UI consumer (users)
 * eg. data changes and updates
 * data -> controller -> view
 */
public class ServiceModel {
  private final boolean isInProgress;
  private final boolean isResult;
  private final boolean isComplete;
  private final boolean isError;
  private final String errorMessage;
  private final BluetoothDevice device;

  private ServiceModel(boolean isInProgress,
                       boolean isResult,
                       boolean isComplete,
                       boolean isError,
                       String errorMessage,
                       BluetoothDevice device) {
    this.isInProgress = isInProgress;
    this.isResult = isResult;
    this.isComplete = isComplete;
    this.isError = isError;
    this.errorMessage = errorMessage;
    this.device = device;
  }

  public static ServiceModel inProgress() {
    return new ServiceModel(true, false, false, false, "no_error", null);
  }

  public static ServiceModel result(BluetoothDevice device) {
    return new ServiceModel(true, true, false, false, "no_error", device);
  }

  public static ServiceModel complete() {
    return new ServiceModel(false, false, true, false, "no_error", null);
  }

  public static ServiceModel error(String errorMessage) {
    return new ServiceModel(false, false,false, true, errorMessage, null);
  }

  public boolean isResult() {
    return isResult;
  }

  public BluetoothDevice getDevice() {
    return device;
  }

  public boolean isInProgress() {
    return isInProgress;
  }

  public boolean isComplete() {
    return isComplete;
  }

  public boolean isError() {
    return isError;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
