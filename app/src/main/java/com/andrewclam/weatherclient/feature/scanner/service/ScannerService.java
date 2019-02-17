package com.andrewclam.weatherclient.feature.scanner.service;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Framework Service {@link DaggerService} implementation of {@link ScannerContract.Service}
 */
public class ScannerService extends DaggerService implements ScannerContract.Service {

  @Inject
  ScannerContract.Controller mController;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  @Inject
  public ScannerService() {
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void onCreate() {
    mController.start();

    mCompositeDisposable.add(mController.getModel().subscribe(model -> {
      if (model.isComplete()) {
        showComplete();
      } else if (model.isInProgress()) {
        showInProgress();
      } else if (model.isError()) {
        showError(model.getErrorMessage());
      }
      if (model.isResult()) {
        showDevice(model.getDevice());
      }
    }));
    super.onCreate();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    mController.stop();
    mCompositeDisposable.clear();
    super.onDestroy();
  }

  @Override
  public void showInProgress() {
    Timber.d("Is in progress...");
  }

  @Override
  public void showComplete() {
    Timber.d("Is complete.");
  }

  @Override
  public void showError(String error) {
    Timber.e("Is error. %s", error);
  }

  @Override
  public void showDevice(BluetoothDevice device) {
    Timber.d("Device found %s", device.getAddress());
  }
}
