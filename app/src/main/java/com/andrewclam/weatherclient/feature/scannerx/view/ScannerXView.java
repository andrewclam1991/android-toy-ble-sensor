package com.andrewclam.weatherclient.feature.scannerx.view;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.feature.scannerx.data.event.ScannerXEventDataSource;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;
import com.andrewclam.weatherclient.feature.scannerx.service.ScannerXService;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;

public class ScannerXView extends DaggerAppCompatActivity implements ScannerXContract.View, View.OnClickListener {

  @Inject
  ScannerXEventDataSource mEventDataSource;

  @Inject
  ScannerXContract.Presenter mPresenter;

  private final CompositeDisposable mCompositeDisposable;

  // Note: Android View Bindings
  @BindView(R.id.scanner_start_scan_btn)
  Button mStartScannerBtn;

  @BindView(R.id.scanner_stop_scan_btn)
  Button mStopScannerBtn;

  @Inject
  public ScannerXView() {
    mCompositeDisposable = new CompositeDisposable();
  }


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_scanner);
    ButterKnife.bind(this);
    mStartScannerBtn.setOnClickListener(this);
    mStopScannerBtn.setOnClickListener(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Intent intent = new Intent(this, ScannerXService.class);
    startService(intent);

    mCompositeDisposable.add(mPresenter.getModel()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onNextResult, this::onError)
    );
  }

  @Override
  protected void onPause() {
    super.onPause();
    mCompositeDisposable.clear();
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
