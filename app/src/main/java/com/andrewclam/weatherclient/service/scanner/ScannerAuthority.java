package com.andrewclam.weatherclient.service.scanner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.service.scanner.ScannerContract.Service;
import com.andrewclam.weatherclient.view.scanner.ScannerFragment;
import com.andrewclam.weatherclient.view.scanner.ScannerViewContract;
import com.andrewclam.weatherclient.view.util.ActivityUtils;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

/**
 * Framework {@link DaggerAppCompatActivity} that acts as the Authority
 * <p>
 * Responsibilities:
 * - handles requests for explicit user permissions.
 */
public class ScannerAuthority extends DaggerAppCompatActivity implements ScannerContract.Authority {

  // Request Codes
  private static final int REQUEST_ENABLE_BT = 1001;
  private static final int REQUEST_BT_PERMISSION = 1002;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner);
  }

  @Override
  public void requestBluetoothAdapterSettings() {
    Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);
  }

  @Override
  public void requestBluetoothPermissions() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.ACCESS_COARSE_LOCATION)) {
      Timber.d("should show request permission rationale.");
      // Show an explanation to the user *asynchronously* -- don't block
      // this thread waiting for the user's response! After the user
      // sees the explanation, try again to request the permission.
      // TODO implement snackbar showing user of coarse location is required for ble
    } else {
      // No explanation needed; request the permission
      Timber.d("call to request permissions.");
      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
          REQUEST_BT_PERMISSION);
    }
  }

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_ENABLE_BT:
        requestBluetoothAdapterSettings();
        break;
      case REQUEST_BT_PERMISSION:
        requestBluetoothPermissions();
        break;
      default:
        Timber.w("Invalid request code.");
    }
  }
}
