package com.andrewclam.weatherclient.feature.scanner.service;

import android.bluetooth.BluetoothDevice;

import com.andrewclam.weatherclient.feature.scanner.model.ScannerResult;

import io.reactivex.Flowable;

class ScannerContract {
  interface Service {
    void showInProgress();

    void showComplete();

    void showError(String message);

    void showDevice(BluetoothDevice device);
  }

  interface Controller {

    Flowable<ScannerResult> getModel();

    void start();

    void stop();
  }
}
