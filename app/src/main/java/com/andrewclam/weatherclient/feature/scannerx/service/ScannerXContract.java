package com.andrewclam.weatherclient.feature.scannerx.service;

import android.bluetooth.BluetoothDevice;

import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import io.reactivex.Flowable;

class ScannerXContract {
  interface Service {
    void showInProgress();

    void showComplete();

    void showError(String message);

    void showDevice(BluetoothDevice device);
  }

  interface Controller {

    Flowable<ScannerXResult> getModel();

    void start();

    void stop();
  }
}
