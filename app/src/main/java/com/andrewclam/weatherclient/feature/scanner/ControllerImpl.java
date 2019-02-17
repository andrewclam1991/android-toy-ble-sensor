package com.andrewclam.weatherclient.feature.scanner;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.UiThread;

import com.andrewclam.weatherclient.feature.scanner.data.ServiceEventDataSource;
import com.andrewclam.weatherclient.feature.scanner.model.ServiceEventModel;
import com.andrewclam.weatherclient.feature.scanner.model.ServiceModel;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.processors.BehaviorProcessor;
import timber.log.Timber;

class ControllerImpl implements ScannerContract.Controller {

  private final ServiceEventDataSource mEventDataSource;

  private final CompositeDisposable mCompositeDisposable;

  private final BehaviorProcessor<ServiceModel> mServiceModelSource;

  private final BluetoothAdapter.LeScanCallback mScanCallback;

  private final static int SCAN_STATE_IN_PROGRESS = 1;
  private final static int SCAN_STATE_IDLE = 0;
  private int mScanState;

  @Inject
  ControllerImpl(@NonNull ServiceEventDataSource dataSource) {
    mEventDataSource = dataSource;
    mCompositeDisposable = new CompositeDisposable();
    mServiceModelSource = BehaviorProcessor.create();
    mScanCallback = (device, rssi, scanRecord) -> {
      mServiceModelSource.onNext(ServiceModel.result(device));
    };
  }

//  @Override
//  public BehaviorProcessor<ServiceEventModel> getEventSource() {
//    return null;
//  }

  @Override
  public Flowable<ServiceModel> getModel() {
    return mServiceModelSource;
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
  private void onEvent(ServiceEventModel event) {
    switch (event) {
      case START_SCAN:
        handleStartScan();
        break;
      case STOP_SCAN:
        handleStopScan();
        break;
    }
  }

  @UiThread
  private void onError(Throwable throwable) {
    Timber.e(throwable, "Error in service event source.");
    mServiceModelSource.onError(throwable);
  }

  private void handleStartScan() {
    if (mScanState == SCAN_STATE_IN_PROGRESS) {
      Timber.w("Scan is already in progress, start scan ignored.");
      return;
    }
    mScanState = SCAN_STATE_IN_PROGRESS;
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    mServiceModelSource.onNext(ServiceModel.inProgress());
    adapter.startLeScan(mScanCallback);
  }

  private void handleStopScan() {
    if (mScanState == SCAN_STATE_IDLE) {
      Timber.w("Scan is already idle, stop scan makes no sense.");
      return;
    }
    mScanState = SCAN_STATE_IDLE;
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    adapter.stopLeScan(mScanCallback);
    mServiceModelSource.onNext(ServiceModel.complete());

  }
}
