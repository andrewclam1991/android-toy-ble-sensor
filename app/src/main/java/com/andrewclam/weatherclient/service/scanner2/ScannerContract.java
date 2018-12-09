package com.andrewclam.weatherclient.service.scanner2;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

/**
 * Reactive MVVM style bluetooth device scanner contract
 */
public class ScannerContract {

  interface View {
    void showDevice(BluetoothDevice device);
    void showIsScanning(boolean isScanning);
    void showScanError(Throwable throwable);
  }

  interface ViewModel {
    PublishProcessor<Boolean> scanCommandPublisher();
    PublishProcessor<BluetoothDevice> deviceSelectionPublisher();
    Flowable<BluetoothDevice> getDevices();
    Flowable<Boolean> getIsScanning();
  }

  interface Model {
    Flowable<BluetoothDevice> getDevices();
  }

}
