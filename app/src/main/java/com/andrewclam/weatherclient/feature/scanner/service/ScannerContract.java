package com.andrewclam.weatherclient.feature.scanner.service;

import android.bluetooth.BluetoothDevice;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceResultModel;

import io.reactivex.Flowable;

public class ScannerContract {
  interface Service {
    void showInProgress();

    void showComplete();

    void showError(String message);

    void showDevice(BluetoothDevice device);
  }

  interface Controller {

    Flowable<ServiceResultModel> getModel();

    void start();

    void stop();
  }
}
