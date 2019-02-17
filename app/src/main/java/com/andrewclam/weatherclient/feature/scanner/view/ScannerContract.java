package com.andrewclam.weatherclient.feature.scanner.view;

import android.bluetooth.BluetoothDevice;

import com.andrewclam.weatherclient.feature.scanner.model.ScannerResult;

import io.reactivex.Flowable;

class ScannerContract {
  interface View {
    void showInProgress(boolean isInProgress);
    void showDeviceResult(BluetoothDevice device);
    void showIsComplete(boolean isComplete);
    void showIsError(boolean isError);
    void showErrorMessage(String errorMessage);
  }

  interface Presenter {
    Flowable<ScannerResult> getModel();
  }
}
