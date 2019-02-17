package com.andrewclam.weatherclient.feature.scanner.service;

import android.app.Notification;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.andrewclam.weatherclient.feature.scanner.model.ScannerEvent;
import com.google.common.base.Strings;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.andrewclam.weatherclient.feature.scanner.service.ScannerNotification.SCANNER_NOTIFICATION_ID;

/**
 * Framework Service {@link DaggerService} implementation of {@link ScannerContract.Service}
 */
public class ScannerService extends DaggerService implements ScannerContract.Service {

  public static int SCANNER_SERVICE_REQUEST_CODE = 5558;

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
    super.onCreate();
    mController.start();
    mCompositeDisposable.add(mController.getModel()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(model -> {
          Notification notification = ScannerNotification.build(ScannerService.this, model);
          startForeground(SCANNER_NOTIFICATION_ID, notification);

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
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (isStartCommandWithActionValid(intent, flags, startId)) {
      @ScannerEvent String event = intent.getAction();
      Objects.requireNonNull(event, "event is empty or null");
      switch (event) {
        case ScannerEvent.START_SCAN:
          mController.start();
        case ScannerEvent.STOP_SCAN:
          mController.stop();
        default:
          throw new UnsupportedOperationException("Unrecognized action" + event);
      }
    }
    return super.onStartCommand(intent, flags, startId);
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

  /**
   * Runtime sanity check for a valid service start command
   *
   * @param intent android intent
   * @param flags android flags
   * @param startId request code, check for {@link #SCANNER_SERVICE_REQUEST_CODE}
   * @return true if valid, false otherwise.
   */
  private boolean isStartCommandWithActionValid(Intent intent, int flags, int startId) {
    if (intent == null) {
      Timber.w("Intent is null");
      return false;
    }

    if (startId != SCANNER_SERVICE_REQUEST_CODE) {
      Timber.d("Expected [%s] but got [%s] instead.", SCANNER_SERVICE_REQUEST_CODE, startId);
      Timber.w("Unsupported request code %s", startId);
      return false;
    }

    String action = intent.getAction();
    if (Strings.isNullOrEmpty(action)) {
      Timber.w("Intent contains no action.");
      return false;
    }

    return true;
  }
}
