package com.andrewclam.weatherclient.feature.scannerx.authx.view;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.andrewclam.weatherclient.feature.scannerx.authx.data.AuthXDataSource;

import javax.inject.Inject;

import androidx.core.content.ContextCompat;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class ViewImpl extends DaggerAppCompatActivity implements AuthXContract.View {

  @Inject
  AuthXDataSource mDataSource;

  private final CompositeDisposable mCompositeDisposable;

  public ViewImpl() {
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  protected void onResume() {
    super.onResume();

    // Note: encapsulate the authXCommand with command pattern??
    mCompositeDisposable.add(mDataSource.getAuthXCommand().subscribe(authXCommand -> {
      switch (authXCommand) {
        case IS_BLUETOOTH_LE_AVAILABLE:
          isBluetoothAdapterAvailable();
          break;
        case IS_BLUETOOTH_ADAPTER_AVAILABLE:
          isBluetoothAdapterAvailable();
          break;
        case IS_BLUETOOTH_PERMISSION_GRANTED:
          isBluetoothPermissionGranted();
          break;
        case REQUEST_BLUETOOTH_PERMISSION:
          requestBluetoothPermission();
          break;
        case REQUEST_BLUETOOTH_ADAPTER:
          requestBluetoothAdapter();
          break;
      }
    }));
  }

  @Override
  protected void onPause() {
    super.onPause();
    mCompositeDisposable.clear();
  }

  @Override
  public void isBluetoothLowEnergyAvailable() {
    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
      Timber.e("Bluetooth Low Energy (BLE) is not supported with this device.");
      mDataSource.setHasBluetoothLowEnergy(false);
    } else {
      Timber.d("Bluetooth Low Energy (BLE) is supported with this device.");
      mDataSource.setHasBluetoothLowEnergy(true);
    }
  }

  @Override
  public void isBluetoothAdapterAvailable() {
    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    if (bluetoothManager != null) {
      final BluetoothAdapter adapter = bluetoothManager.getAdapter();
      mDataSource.setHasBluetoothAdapter(adapter != null && adapter.isEnabled());
    } else {
      Timber.w("BluetoothManager not available, device doesn't support bluetooth?");
      mDataSource.setHasBluetoothAdapter(false);
    }
  }

  @Override
  public void isBluetoothPermissionGranted() {
    boolean hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
    mDataSource.setHasBluetoothPermission(hasPermission);
  }

  @Override
  public void requestBluetoothAdapter() {

  }

  @Override
  public void requestBluetoothPermission() {

  }
}
