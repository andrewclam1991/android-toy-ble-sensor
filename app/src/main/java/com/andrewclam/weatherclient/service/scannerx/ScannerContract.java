package com.andrewclam.weatherclient.service.scannerx;

import android.bluetooth.BluetoothDevice;

import io.reactivex.Completable;
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

  /**
   * Handles View inputs by exposing input event publishers (sources) for view to post updates
   * Handles View outputs (transformation) by exposing output event observable
   */
  interface ViewModel {
    // Handles View inputs
    PublishProcessor<Boolean> scanCommandPublisher();

    PublishProcessor<BluetoothDevice> deviceSelectionPublisher();

    // Handles View outputs
    Flowable<BluetoothDevice> getDevices();

    Flowable<Boolean> getIsScanning();
  }

  interface Model {
    Flowable<BluetoothDevice> getDevices();
    Completable cancelGetDevices();
  }

}
