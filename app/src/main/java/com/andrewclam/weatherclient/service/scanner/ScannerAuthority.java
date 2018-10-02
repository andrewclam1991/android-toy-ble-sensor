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
 */

package com.andrewclam.weatherclient.service.scanner;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.andrewclam.weatherclient.R;
import com.google.common.base.Strings;

import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

/**
 * Framework {@link DaggerAppCompatActivity} that acts as the Authority
 * <p>
 * Responsibilities:
 * - handles requests for explicit user permissions.
 */
public class ScannerAuthority extends DaggerAppCompatActivity implements ScannerContract.Authority {

  // Intent Actions
  public static final String INTENT_ACTION_PREFIX = "ScannerAuthority.INTENT_ACTION.";
  public static final String INTENT_ACTION_REQUEST_ENABLE_BLUETOOTH_ADAPTER =
      INTENT_ACTION_PREFIX + "request_enable_bluetooth_adapter";
  public static final String INTENT_ACTION_REQUEST_BLUETOOTH_PERMISSIONS =
      INTENT_ACTION_PREFIX + "request_bluetooth_permissions";

  // Request Codes
  private static final int REQUEST_CODE_ENABLE_BLUETOOTH_ADAPTER = 1001;
  private static final int REQUEST_CODE_BLUETOOTH_PERMISSIONS = 1002;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner);

    Intent intent = getIntent();
    String action = intent.getAction();
    if (Strings.isNullOrEmpty(action)) {
      Timber.e("Authority started without any action, terminate.");
      finish();
    } else {
      switch (action) {
        case INTENT_ACTION_REQUEST_ENABLE_BLUETOOTH_ADAPTER:
          requestEnableBluetoothAdapter();
          break;
        case INTENT_ACTION_REQUEST_BLUETOOTH_PERMISSIONS:
          requestBluetoothPermissions();
          break;
        default:
          Timber.e("Unknown action, terminate.");
          finish();
          break;
      }
    }
  }

  @Override
  public void requestEnableBluetoothAdapter() {
    Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(bluetoothIntent, REQUEST_CODE_ENABLE_BLUETOOTH_ADAPTER);
  }

  @Override
  public void requestBluetoothPermissions() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.ACCESS_COARSE_LOCATION)) {
      Timber.d("should show request permission rationale.");
      View rootView = findViewById(R.id.fragment_container);
      Snackbar.make(rootView, "Must enable bluetooth adapter", Snackbar.LENGTH_INDEFINITE)
          .setAction("Enable", v -> {
            Timber.d("call to request permissions.");
            ActivityCompat.requestPermissions(ScannerAuthority.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_BLUETOOTH_PERMISSIONS);
          })
          .show();
    } else {
      // No explanation needed; request the permission
      Timber.d("call to request permissions.");
      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
          REQUEST_CODE_BLUETOOTH_PERMISSIONS);
    }
  }

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    switch (requestCode) {
      case REQUEST_CODE_ENABLE_BLUETOOTH_ADAPTER:
        if (resultCode == Activity.RESULT_OK) {
          finish();
        } else {
          requestEnableBluetoothAdapter();
        }
        break;
      default:
        Timber.w("Invalid request code.");
        finish();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_BLUETOOTH_PERMISSIONS:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          finish();
        } else {
          requestBluetoothPermissions();
        }
        break;
      default:
        Timber.w("Invalid request code.");
        finish();
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
