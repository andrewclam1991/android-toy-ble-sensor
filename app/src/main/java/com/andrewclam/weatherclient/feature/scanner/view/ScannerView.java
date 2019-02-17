package com.andrewclam.weatherclient.feature.scanner.view;

import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.widget.Button;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.feature.scanner.data.event.ServiceEventDataSource;
import com.andrewclam.weatherclient.feature.scanner.model.ScannerEvent;
import com.andrewclam.weatherclient.feature.scanner.model.ServiceResultModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;

class ScannerView implements ScannerContract.View, View.OnClickListener {

  @Inject
  ServiceEventDataSource mEventDataSource;

  @Inject
  ScannerContract.Presenter mPresenter;

  private final CompositeDisposable mCompositeDisposable;

  private Button mStartScanButton;
  private Button mStopScanButton;

  @Inject
  public ScannerView() {
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
        mEventDataSource.put(ScannerEvent.START_SCAN);
        break;
      case R.id.scanner_stop_scan_btn:
        mEventDataSource.put(ScannerEvent.STOP_SCAN);
        break;
    }
  }

  private void onNextResult(ServiceResultModel model) {
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
