package com.andrewclam.weatherclient.feature.scannerx.view;

import android.bluetooth.BluetoothDevice;

import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import io.reactivex.Flowable;

class ScannerXContract {
  interface View {
    void showInProgress(boolean isInProgress);
    void showDeviceResult(BluetoothDevice device);
    void showIsComplete(boolean isComplete);
    void showIsError(boolean isError);
    void showErrorMessage(String errorMessage);
  }

  interface Presenter {
    Flowable<ScannerXResult> getModel();
  }
}
