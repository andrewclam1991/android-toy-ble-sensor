package com.andrewclam.weatherclient.feature.scanner.model;

import android.bluetooth.BluetoothDevice;

/**
 * Model for UI consumer (users)
 * eg. data changes and updates
 * data -> controller -> view
 */
public class ScannerResult {
  private final boolean isInProgress;
  private final boolean isResult;
  private final boolean isComplete;
  private final boolean isError;
  private final String errorMessage;
  private final BluetoothDevice device;

  private ScannerResult(boolean isInProgress,
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

  public static ScannerResult inProgress() {
    return new ScannerResult(true, false, false, false, "no_error", null);
  }

  public static ScannerResult result(BluetoothDevice device) {
    return new ScannerResult(true, true, false, false, "no_error", device);
  }

  public static ScannerResult complete() {
    return new ScannerResult(false, false, true, false, "no_error", null);
  }

  public static ScannerResult error(String errorMessage) {
    return new ScannerResult(false, false,false, true, errorMessage, null);
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
