package com.andrewclam.weatherclient.feature.scanner.service;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.UiThread;

import com.andrewclam.weatherclient.feature.scanner.data.event.ServiceEventDataSource;
import com.andrewclam.weatherclient.feature.scanner.data.result.ServiceResultDataSource;
import com.andrewclam.weatherclient.feature.scanner.model.ScannerEvent;
import com.andrewclam.weatherclient.feature.scanner.model.ServiceResultModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

class ScannerController implements ScannerContract.Controller {

  private final ServiceEventDataSource mEventDataSource;

  private final ServiceResultDataSource mResultDataSource;

  private final CompositeDisposable mCompositeDisposable;

  private final BluetoothAdapter.LeScanCallback mScanCallback;

  private final static int SCAN_STATE_IN_PROGRESS = 1;
  private final static int SCAN_STATE_IDLE = 0;
  private int mScanState;

  @Inject
  ScannerController(@NonNull ServiceEventDataSource eventDataSource,
                    @NonNull ServiceResultDataSource resultDataSource) {
    mEventDataSource = eventDataSource;
    mResultDataSource = resultDataSource;
    mCompositeDisposable = new CompositeDisposable();
    mScanCallback = (device, rssi, scanRecord) -> mResultDataSource.add(ServiceResultModel.result(device));
  }

  @Override
  public Flowable<ServiceResultModel> getModel() {
    return mResultDataSource.get();
  }

  @Override
  public void start() {
    mCompositeDisposable.add(mEventDataSource.get()
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onEvent, this::onError)
    );
  }

  @Override
  public void stop() {
    mCompositeDisposable.clear();
  }

  @UiThread
  private void onEvent(@ScannerEvent String event) {
    switch (event) {
      case ScannerEvent.START_SCAN:
        startScan();
        break;
      case ScannerEvent.STOP_SCAN:
        stopScan();
        break;
    }
  }

  @UiThread
  private void onError(Throwable throwable) {
    Timber.e(throwable, "Error in service event source.");
  }

  private void startScan() {
    if (mScanState == SCAN_STATE_IN_PROGRESS) {
      Timber.w("Scan is already in progress, start scan ignored.");
      return;
    }
    mScanState = SCAN_STATE_IN_PROGRESS;
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    mResultDataSource.add(ServiceResultModel.inProgress());
    adapter.startLeScan(mScanCallback);

    mCompositeDisposable.add(Completable.fromAction(this::stopScan)
        .delay(10, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io())
        .subscribe()
    );
  }

  private void stopScan() {
    if (mScanState == SCAN_STATE_IDLE) {
      Timber.w("Scan is already idle, stop scan makes no sense.");
      return;
    }
    mScanState = SCAN_STATE_IDLE;
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    adapter.stopLeScan(mScanCallback);
    mResultDataSource.add(ServiceResultModel.complete());
  }
}
