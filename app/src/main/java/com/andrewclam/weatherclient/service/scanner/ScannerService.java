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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
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
    if (hasPermissions()) {
      Timber.d("Start scan, permission(s) granted.");
      mController.startScan();
    } else if (mAuthority != null) {
      Timber.d("Abort scan, permission(s) not granted, ask Authority for permission(s).");
      mAuthority.checkBluetoothPermissions();
    } else {
      Timber.w("Abort scan, Authority unavailable to ask for permission(s).");
    }
  }

  @Override
  public void stopScan() {
    mController.stopScan();
  }

  @Override
  public void startService() {
    // TODO implement framework start service and start foreground notification
    if (mAuthority != null){
      mAuthority.checkBluetoothPermissions();
      mAuthority.checkBluetoothAdapterSettings();
    }
    startScan();
  }

  @Override
  public void stopService() {
    stopScan();
    stopForeground(true);
    stopSelf();
  }

  @Nonnull
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
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
}
