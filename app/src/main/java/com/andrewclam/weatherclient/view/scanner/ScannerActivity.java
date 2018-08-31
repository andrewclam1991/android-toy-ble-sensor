package com.andrewclam.weatherclient.view.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.service.scanner.ScannerContract;
import com.andrewclam.weatherclient.view.util.ActivityUtils;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Framework {@link DaggerAppCompatActivity} that acts as the Authority
 * Responsible for interfacing with User to get proper permissions for downstream workers.
 */
public class ScannerActivity extends DaggerAppCompatActivity implements ScannerContract.Authority {

  @Inject
  Lazy<ScannerFragment> mScannerFragmentProvider;

  @Nullable
  private BluetoothAdapter mBluetoothAdapter;

  private static final int REQUEST_ENABLE_BT = 1234;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner);
  }

  @Override
  public void checkBluetoothAdapterSettings() {
    // Use this check to determine whether BLE is supported on the device. Then
    // you can selectively disable BLE-related features.
    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
      Timber.w("BLE Not Supported with this device.");
      Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
      finish();
    }

    // Initializes Bluetooth adapter.
    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    checkNotNull(bluetoothManager, "Requires valid bluetooth manager");
    mBluetoothAdapter = bluetoothManager.getAdapter();

    // Ensures Bluetooth is available on the device and it is enabled. If not,
    // displays a dialog requesting user permission to enable Bluetooth.
    if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
      Timber.w("Bluetooth is not available or is disabled, request user to enable bluetooth");
      Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    } else {
      Timber.d("Bluetooth is available and turned on.");
      loadScannerFragment();
    }
  }

  @Override
  public void checkBluetoothPermissions() {
    // TODO - Request permission
  }

  @NonNull
  @Override
  public BluetoothAdapter getBluetoothAdapter() {
    checkNotNull(mBluetoothAdapter, "Requires valid bluetooth adapter");
    return mBluetoothAdapter;
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
        if (resultCode == RESULT_OK) {
          checkBluetoothAdapterSettings();
        } else {
          Timber.w("User denied turning on bluetooth, scan can't continue");
          finish();
        }
        break;
      default:
        Timber.w("Invalid request code.");
    }
  }

  /**
   * Internal
   * Authority mediator to load an instance of collaborator
   */
  private void loadScannerFragment() {
    ScannerFragment fragment = (ScannerFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment_container);

    if (fragment == null) {
      // Create the fragment
      fragment = mScannerFragmentProvider.get();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), fragment, R.id.fragment_container);
    }
  }
}
