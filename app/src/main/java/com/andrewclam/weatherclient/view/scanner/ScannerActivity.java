package com.andrewclam.weatherclient.view.scanner;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.service.scanner.ScannerContract;
import com.andrewclam.weatherclient.view.util.ActivityUtils;
import com.andrewclam.weatherclient.view.util.FragmentServiceBinderListener;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

public class ScannerActivity extends DaggerAppCompatActivity implements ScannerContract.View, FragmentServiceBinderListener {

  @Nullable
  private BluetoothAdapter mBluetoothAdapter;

  @Inject
  Lazy<ScannerFragment> mScannerFragmentProvider;

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
      Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
      finish();
    }

    // Initializes Bluetooth adapter.
    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    mBluetoothAdapter = bluetoothManager.getAdapter();

    // Ensures Bluetooth is available on the device and it is enabled. If not,
    // displays a dialog requesting user permission to enable Bluetooth.
    if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
      Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    } else {
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
          loadScannerFragment();
        } else {
          Timber.w("User denied turning on bluetooth, scan can't continue");
          finish();
        }
        break;
      default:
        Timber.w("Invalid request code.");
    }
  }

  @Override
  public <S extends Service> void onRequestBindService(@NonNull Class<S> serviceClass, @NonNull ServiceConnection serviceConnection, int flags) {
    Intent intent = new Intent(this, serviceClass);
    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    Timber.d("Called parent activity to bind View to Service class.");
  }

  @Override
  public void onRequestUnbindService(@NonNull ServiceConnection serviceConnection) {
    unbindService(serviceConnection);
    Timber.d("Called parent activity to unbind View from Service class.");
  }

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
