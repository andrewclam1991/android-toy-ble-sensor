package com.andrewclam.weatherclient.feature.scannerx.view;

import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.widget.Button;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.feature.scannerx.data.event.ScannerEventDataSource;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;

public class ScannerXView extends DaggerAppCompatActivity implements ScannerXContract.View, View.OnClickListener {

  @Inject
  ScannerEventDataSource mEventDataSource;

  @Inject
  ScannerXContract.Presenter mPresenter;

  private final CompositeDisposable mCompositeDisposable;

  // Note: Android View Bindings
  private Button mStartScanButton;
  private Button mStopScanButton;

  @Inject
  public ScannerXView() {
    mCompositeDisposable = new CompositeDisposable();
  }

  public void onCreate() {
    mStartScanButton.setOnClickListener(this);
    mStopScanButton.setOnClickListener(this);

    mCompositeDisposable.add(mPresenter.getModel()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onNextResult, this::onError)
    );
  }

  @Override
  public void showInProgress(boolean isInProgress) {

  }

  @Override
  public void showIsComplete(boolean isComplete) {

  }

  @Override
  public void showIsError(boolean isError) {

  }

  @Override
  public void showErrorMessage(String errorMessage) {

  }

  @Override
  public void showDeviceResult(BluetoothDevice device) {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.scanner_start_scan_btn:
        mEventDataSource.put(ScannerXEvent.START_SCAN);
        break;
      case R.id.scanner_stop_scan_btn:
        mEventDataSource.put(ScannerXEvent.STOP_SCAN);
        break;
    }
  }

  private void onNextResult(ScannerXResult model) {
    showInProgress(model.isInProgress());
    showIsComplete(model.isComplete());
    showIsError(model.isError());
    if (model.isError()) {
      showErrorMessage(model.getErrorMessage());
    }
    if (model.isResult()) {
      showDeviceResult(model.getDevice());
    }
  }

  private void onError(Throwable throwable) {
    throw new OnErrorNotImplementedException(throwable);
  }
}
