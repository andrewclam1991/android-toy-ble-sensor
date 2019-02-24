package com.andrewclam.weatherclient.feature.scannerx.authority.view;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

class ViewImpl extends DaggerAppCompatActivity implements AuthorityContract.View {

  @Inject
  AuthorityContract.Controller mController;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  @Inject
  public ViewImpl() {
    // Required empty constructor
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mCompositeDisposable.add(mController.getEvent()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> {
          switch (event) {
            case CHECK_HAS_BLE_FEATURE:
              mController.setHasBleFeature(hasBleFeature());
              break;
            case CHECK_HAS_BT_PERMISSION:
              mController.setHasPermissions(hasPermissions());
              break;
            case CHECK_HAS_BT_ADAPTER_ON:
              mController.setHasBtAdapter(hasBTAdapter());
              break;
          }
        }));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mCompositeDisposable.clear();
  }

  @Override
  public boolean hasBleFeature() {
    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
      Timber.e("Bluetooth Low Energy (BLE) is not supported with this device.");
      return false;
    } else {
      Timber.d("Bluetooth Low Energy (BLE) is supported with this device.");
      return true;
    }
  }

  @Override
  public boolean hasBTAdapter() {
    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    if (bluetoothManager != null) {
      final BluetoothAdapter adapter = bluetoothManager.getAdapter();
      return adapter != null && adapter.isEnabled();
    } else {
      Timber.w("BluetoothManager not available, device doesn't support bluetooth?");
      return false;
    }
  }

  @Override
  public boolean hasPermissions() {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
  }
}
