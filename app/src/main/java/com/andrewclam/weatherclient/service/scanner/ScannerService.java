/*
 * Copyright 2018 Andrew Chi Heng Lam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * com.andrewclam.weatherclient.service.pairing.PairingService
 */

package com.andrewclam.weatherclient.service.scanner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import dagger.android.DaggerService;
import timber.log.Timber;

/**
 * Simple Service class
 * Delegates scanning for ble peripheral devices to its {@link ScannerContract.Controller}
 */
public final class ScannerService extends DaggerService implements ScannerContract.Service {

  @Inject
  ScannerContract.Controller mController;

  @Nonnull
  private final ServiceBinder mBinder = new ServiceBinder();

  @Nullable
  private ScannerContract.Authority mAuthority;

  @Inject
  public ScannerService() {
    // Required no-arg constructor
  }

  @Override
  public void setAuthority(@Nonnull ScannerContract.Authority authority) {
    mAuthority = authority;
  }

  @Override
  public void dropAuthority() {
    mAuthority = null;
  }

  @Override
  public void startScan() {
    if (mAuthority != null) {
      if (!hasPermissions()) {
        Timber.d("Abort scan, permission(s) not granted, ask Authority for permission(s).");
        mAuthority.checkBluetoothPermissions();
        return;
      }

      if (!hasValidSettings()){
        Timber.d("Abort scan, setting(s) not satisfied, ask Authority for setting(s).");
        mAuthority.checkBluetoothAdapterSettings();
        return;
      }

      Timber.d("Start scan, permission(s) granted and setting(s) are valid.");
      mController.startScan();
    } else {
      Timber.e("Authority not available to start scan.");
    }
  }

  @Override
  public void stopScan() {
    mController.stopScan();
  }

  @Nonnull
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public void startService() {
    // TODO implement framework start service and start foreground notification
    Timber.d("startService() called to start foreground service");

    Intent intent = new Intent(getApplicationContext(), ScannerService.class);
    // Check android version for foreground notification requirement
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startForegroundService(intent);
    } else {
      startService(intent);
    }
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Timber.d("Service onStartCommand() called, service started");
    // Check android version for foreground notification requirement
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Timber.d("> Android O, post foreground notification to keep service alive.");
      startForeground(ScannerNotification.SCANNER_NOTIFICATION_ID, ScannerNotification.build(getApplicationContext()));
    }
    mController.setService(this);
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void stopService() {
    stopScan();
    mController.dropService();
    stopForeground(true);
    stopSelf();
  }

  @Override
  public void cleanup() {
    mController.cleanup();
  }

  /**
   * Class used for the client Binder.  Because we know this service always
   * runs in the same process as its clients, we don't need to deal with IPC.
   */
  public final class ServiceBinder extends Binder {
    public ScannerContract.Service getService() {
      return ScannerService.this;
    }
  }

  /**
   * Internal
   * Checks if the current application has all the permissions to do the required operations
   * @return flags whether the app has all the necessary permissions.
   */
  private boolean hasPermissions() {
    return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED  &&
        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
  }

  private boolean hasValidSettings(){
    BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    BluetoothAdapter adapter = bluetoothManager.getAdapter();
    return adapter != null && adapter.isEnabled();
  }
}
