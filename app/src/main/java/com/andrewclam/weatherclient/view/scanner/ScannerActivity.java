package com.andrewclam.weatherclient.view.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.service.scanner.ScannerContract;
import com.andrewclam.weatherclient.service.scanner.ScannerContract.Service;
import com.andrewclam.weatherclient.service.scanner.ScannerService;
import com.andrewclam.weatherclient.view.util.ActivityUtils;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Framework {@link DaggerAppCompatActivity} that acts as the Authority
 * Responsibilities:
 * - interfacing with framework {@link ScannerContract.Service}.
 * - handles framework service request for authorization.
 * - encapsulate the framework permission logics.
 */
public class ScannerActivity extends DaggerAppCompatActivity implements ScannerContract.Authority {

  private static final int REQUEST_ENABLE_BT = 1234;

  @Nullable
  private BluetoothAdapter mBluetoothAdapter;

  @Nonnull
  private final ServiceConnection mScannerServiceConnection = getServiceConnection();

  @Nullable
  private Service mScannerService;

  private boolean mScannerServiceBound = false;

  @Inject
  Lazy<ScannerFragment> mScannerFragmentProvider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner);
    loadViewScannerFragment();
  }

  @Override
  protected void onStart() {
    super.onStart();
    Timber.d("Authority View started, call to bind View to ScannerService");
    Intent service = new Intent(this, ScannerService.class);
    bindService(service,mScannerServiceConnection,Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
    super.onStop();
    Timber.d("Authority View stopped, call to unbind View from ScannerService");
    if (mScannerService != null && mScannerServiceBound){
      mScannerService.dropAuthority();
      unbindService(mScannerServiceConnection);
      mScannerServiceBound = false;
    }
  }

  @Override
  public void checkBluetoothAdapterSettings() {
    // Use this check to determine whether BLE is supported on the device. Then
    // you can selectively disable BLE-related features.
    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
      Timber.w("Bluetooth Low Energy (BLE) is not supported with this device.");
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
      Timber.w("Bluetooth adapter is not available or is disabled, request user to enable bluetooth");
      Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);
    } else {
      Timber.d("Bluetooth adapter is available and turned on.");
      if (mScannerService != null && mScannerServiceBound){
        mScannerService.startScan();
      }
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
          Timber.d("User enabled and turned on Bluetooth, scan can start.");
          checkBluetoothAdapterSettings();
        } else {
          Timber.w("User denied turning on Bluetooth, scan can't continue.");
          finish();
        }
        break;
      default:
        Timber.w("Invalid request code.");
    }
  }

  /**
   * Internal
   * Loads the View class
   */
  private void loadViewScannerFragment() {
    ScannerFragment fragment = (ScannerFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment_container);

    if (fragment == null) {
      // Create the fragment
      fragment = mScannerFragmentProvider.get();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), fragment, R.id.fragment_container);
    }
  }

  /**
   * Internal
   * Defines behaviors on service connection events
   * @return an instance of {@link ServiceConnection}
   */
  @Nonnull
  private ServiceConnection getServiceConnection(){
    return new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName name, IBinder service) {
        ScannerService.ServiceBinder binder = (ScannerService.ServiceBinder) service;
        mScannerService = binder.getService();
        mScannerService.setAuthority(ScannerActivity.this);
        mScannerService.startScan();
        mScannerServiceBound = true;
        Timber.d("Authority View connected and bound to ScannerService.");
      }

      @Override
      public void onServiceDisconnected(ComponentName name) {
        mScannerServiceBound = false;
        Timber.d("Authority View disconnected and unbound from ScannerService.");
      }
    };
  }
}
