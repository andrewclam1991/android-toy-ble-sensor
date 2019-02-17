package com.andrewclam.weatherclient.feature.scanner.model;

import android.bluetooth.BluetoothDevice;

/**
 * Model for UI consumer (users)
 * eg. data changes and updates
 * data -> controller -> view
 */
public class ServiceResultModel {
  private final boolean isInProgress;
  private final boolean isResult;
  private final boolean isComplete;
  private final boolean isError;
  private final String errorMessage;
  private final BluetoothDevice device;

  private ServiceResultModel(boolean isInProgress,
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

  public static ServiceResultModel inProgress() {
    return new ServiceResultModel(true, false, false, false, "no_error", null);
  }

  public static ServiceResultModel result(BluetoothDevice device) {
    return new ServiceResultModel(true, true, false, false, "no_error", device);
  }

  public static ServiceResultModel complete() {
    return new ServiceResultModel(false, false, true, false, "no_error", null);
  }

  public static ServiceResultModel error(String errorMessage) {
    return new ServiceResultModel(false, false,false, true, errorMessage, null);
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
