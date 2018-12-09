package com.andrewclam.weatherclient.service.scanner2;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class Scanner2Activity extends AppCompatActivity implements ScannerContract.View {

  @Inject
  ScannerContract.ViewModel mViewModel;

  @Inject
  BaseSchedulerProvider mSchedulerProvider;

  @NonNull
  private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner2);
  }

  @Override
  protected void onResume() {
    super.onResume();
    showIsScanning(true);
    mViewModel.scanCommandPublisher().onNext(true);

    Disposable devicesDisposable = mViewModel.getDevices()
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(device -> {
        }, this::showScanError);

    mCompositeDisposable.add(devicesDisposable);

    Disposable disposable = mViewModel.getIsScanning()
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(this::showIsScanning, this::showScanError);

    mCompositeDisposable.add(disposable);
  }

  @Override
  protected void onPause() {
    super.onPause();
    showIsScanning(false);
    mViewModel.scanCommandPublisher().onNext(false);
    mCompositeDisposable.clear();
  }

  @Override
  public void showDevice(BluetoothDevice device) {
    Toast.makeText(this, device != null ? device.getAddress() : "Null device", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showIsScanning(boolean isScanning) {
    Toast.makeText(this, isScanning ? "Scanning" : "Scan Stopped", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showScanError(Throwable throwable) {

  }
}
