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
import android.widget.Toast;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.view.scanner.ScannerActivity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import dagger.android.DaggerService;
import timber.log.Timber;

/**
 * Simple Service class
 * Delegates scanning for ble peripheral devices to its {@link ScannerContract.Controller}
 */
public final class ScannerService extends DaggerService implements ScannerContract.Service {

  // Intent Actions

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

  /**
   * Helper Static call to framework to start service and handle work with intent actions
   * Note: Handles work only though intent actions
   *
   * @param applicationContext required application context
   */
  public static void startService(@Nonnull Context applicationContext) {
    Timber.d("static startService() called to start foreground service");
    Intent intent = new Intent(applicationContext, ScannerService.class);
    // Check android version for foreground notification requirement
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      applicationContext.startForegroundService(intent);
    } else {
      applicationContext.startService(intent);
    }
  }

  @Nonnull
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public void startService() {
    Timber.d("startService() called to start foreground service.");
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
    Timber.d("Service onStartCommand() called, service started by framework.");
    // TODO implement handle handle work by intent actions
    return super.onStartCommand(intent, flags, startId);
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
    if (isScanDependenciesSatisfied()) {
      Timber.d("Start scan, all requirements satisfied.");
      // Check android version for foreground notification requirement
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Timber.d("SDK Version > Android O, post foreground notification to keep service alive.");
        startForeground(ScannerNotification.SCANNER_NOTIFICATION_ID, ScannerNotification.build(getApplicationContext()));
      }
      mController.startScan();
    }else{
      Timber.w("Abort scan, at least one requirement is not satisfied.");
    }
  }

  @Override
  public void stopScan() {
    mController.stopScan();
  }

  @Override
  public void stopService() {
    mController.dropService();
    stopScan();
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
   * Checks if framework dependencies are satisfied for scanning
   * @return flag that indicate if runtime dependencies are satisfied
   */
  private boolean isScanDependenciesSatisfied() {
    if (!hasBleFeature()) {
      Timber.w("Abort scan, required device hardware ble feature is missing.");
      return false;
    }

    if (!hasPermissions()) {
      Timber.w("Abort scan, permission(s) not granted.");
      if (mAuthority != null) {
        Timber.d("Authority available, ask for permission(s)");
        mAuthority.requestBluetoothPermissions();
      } else {
        // TODO start authority activity with request bluetooth permissions intent
        Intent authorityIntent = new Intent(this, ScannerAuthority.class);
        authorityIntent.setAction("request_bluetooth_permissions");
        startActivity(authorityIntent);
      }
      return false;
    }

    if (!hasValidSettings()) {
      Timber.w("Abort scan, setting(s) not satisfied, ask Authority for setting(s).");
      if (mAuthority != null){
        mAuthority.requestBluetoothAdapterSettings();
      } else {
        // TODO start authority activity with request bluetooth adapter settings change intent
        Intent authorityIntent = new Intent(this, ScannerAuthority.class);
        authorityIntent.setAction("request_bluetooth_adapter_settings");
        startActivity(authorityIntent);
      }
      return false;
    }

    // pass procedural test
    return true;
  }

  /**
   * Internal
   * Checks if the Service can use ble features
   *
   * @return flags whether the device supports Bluetooth Low Energy (BLE)
   */
  private boolean hasBleFeature() {
    // Use this check to determine whether BLE is supported on the device. Then
    // you can selectively disable BLE-related features.
    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
      Timber.w("Bluetooth Low Energy (BLE) is not supported with this device.");
      Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
      return false;
    } else {
      Timber.d("Bluetooth Low Energy (BLE) is supported with this device.");
      return true;
    }
  }

  /**
   * Internal
   * Checks if the current application has all the permissions to do the required operations
   *
   * @return flags whether the app currently has the necessary permissions.
   */
  private boolean hasPermissions() {
    return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Internal
   * Checks if the Service has the correct settings to do the required operation(s)
   *
   * @return flags whether the app currently has the necessary settings.
   */
  private boolean hasValidSettings() {
    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    if (bluetoothManager != null) {
      final BluetoothAdapter adapter = bluetoothManager.getAdapter();
      return adapter != null && adapter.isEnabled();
    } else {
      Timber.w("BluetoothManager not available, device doesn't support bluetooth?");
      return false;
    }
  }
}
