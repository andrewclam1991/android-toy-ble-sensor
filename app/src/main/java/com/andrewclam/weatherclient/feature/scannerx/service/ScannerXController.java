package com.andrewclam.weatherclient.feature.scannerx.service;

import android.bluetooth.BluetoothAdapter;

import com.andrewclam.weatherclient.feature.scannerx.authx.data.AuthXDataSource;
import com.andrewclam.weatherclient.feature.scannerx.authx.model.AuthXResult;
import com.andrewclam.weatherclient.feature.scannerx.data.event.ScannerXEventDataSource;
import com.andrewclam.weatherclient.feature.scannerx.data.result.ScannerXResultDataSource;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.UiThread;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class ScannerXController implements ScannerXContract.Controller {

  private final AuthXDataSource mAuthXDataSource;

  private final ScannerXEventDataSource mEventDataSource;

  private final ScannerXResultDataSource mResultDataSource;

  private final CompositeDisposable mEventDisposables;

  private final CompositeDisposable mAutoStopDisposables;

  private final BluetoothAdapter.LeScanCallback mScanCallback;

  private final static int SCAN_STATE_IN_PROGRESS = 1;
  private final static int SCAN_STATE_IDLE = 0;
  private int mScanState;

  @Inject
  ScannerXController(@NonNull AuthXDataSource authXDataSource,
                     @NonNull ScannerXEventDataSource eventDataSource,
                     @NonNull ScannerXResultDataSource resultDataSource) {
    mAuthXDataSource = authXDataSource;
    mEventDataSource = eventDataSource;
    mResultDataSource = resultDataSource;
    mEventDisposables = new CompositeDisposable();
    mAutoStopDisposables = new CompositeDisposable();
    mScanCallback = (device, rssi, scanRecord) -> mResultDataSource.add(ScannerXResult.result(device));
  }

  @Override
  public Flowable<ScannerXResult> getModel() {
    return mResultDataSource.get();
  }

  @Override
  public void start() {
    // Note: without rx-authorization
    //    mEventDisposables.add(mEventDataSource.get()
    //        .subscribeOn(AndroidSchedulers.mainThread())
    //        .subscribe(this::onEvent, this::onError)
    //    );

    // Note: with rx-authorization
    mEventDisposables.add(mAuthXDataSource.getAuthXResult()
        .filter(AuthXResult::isAuthorized)
        .flatMap(r -> mEventDataSource.get())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onEvent, this::onError)
    );

    mEventDisposables.add(mAuthXDataSource.getAuthXResult()
        .filter(AuthXResult::isDenied)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(denied -> stopScan(), this::onError)
    );
  }

  @Override
  public void stop() {
    mEventDisposables.clear();
    mAutoStopDisposables.clear();
  }

  @UiThread
  private void onEvent(@ScannerXEvent String event) {
    switch (event) {
      case ScannerXEvent.START_SCAN:
        startScan();
        break;
      case ScannerXEvent.STOP_SCAN:
        stopScan();
        break;
    }
  }

  @UiThread
  private void onError(Throwable throwable) {
    Timber.e(throwable, "Error in service event source.");
    mResultDataSource.add(ScannerXResult.error(throwable.getMessage()));
  }

  private void startScan() {
    if (mScanState == SCAN_STATE_IN_PROGRESS) {
      Timber.w("Scan is already in progress, start scan ignored.");
      return;
    }
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    mResultDataSource.add(ScannerXResult.inProgress());
    adapter.startLeScan(mScanCallback);

    mAutoStopDisposables.add(Completable.timer(10, TimeUnit.SECONDS)
        .subscribe(this::stopScan)
    );

    mScanState = SCAN_STATE_IN_PROGRESS;
  }

  private void stopScan() {
    if (mScanState == SCAN_STATE_IDLE) {
      Timber.w("Scan is already idle, stop scan makes no sense.");
      return;
    }
    mAutoStopDisposables.clear();
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    adapter.stopLeScan(mScanCallback);
    mResultDataSource.add(ScannerXResult.complete());
    mScanState = SCAN_STATE_IDLE;
  }
}
