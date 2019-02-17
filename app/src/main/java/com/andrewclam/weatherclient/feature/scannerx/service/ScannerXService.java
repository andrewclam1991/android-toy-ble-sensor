package com.andrewclam.weatherclient.feature.scannerx.service;

import android.app.Notification;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;

import com.andrewclam.weatherclient.feature.scannerx.data.event.ScannerXEventDataSource;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import dagger.android.DaggerService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.andrewclam.weatherclient.feature.scannerx.service.ScannerXNotification.SCANNER_NOTIFICATION_ID;

/**
 * Framework Service {@link DaggerService} implementation of {@link ScannerXContract.Service}
 */
public class ScannerXService extends DaggerService implements ScannerXContract.Service {

  @Inject
  ScannerXEventDataSource mEventDataSource;

  @Inject
  ScannerXContract.Controller mController;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  @Inject
  public ScannerXService() {
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mController.start();
    mCompositeDisposable.add(mController.getModel()
        .distinctUntilChanged()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(model -> {
          Notification notification = ScannerXNotification.build(ScannerXService.this, model);
          startForeground(SCANNER_NOTIFICATION_ID, notification);

          if (model.isComplete()) {
            showComplete();
          } else if (model.isInProgress()) {
            showInProgress();
          } else if (model.isError()) {
            showError(model.getErrorMessage());
            stopSelf();
          }
          if (model.isResult()) {
            showDevice(model.getDevice());
          }
        }));
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (isStartCommandWithActionValid(intent)) {
      @ScannerXEvent String event = intent.getAction();
      Objects.requireNonNull(event, "event is empty or null");
      switch (event) {
        case ScannerXEvent.START_SCAN:
          mEventDataSource.put(ScannerXEvent.START_SCAN);
          break;
        case ScannerXEvent.STOP_SCAN:
          mEventDataSource.put(ScannerXEvent.STOP_SCAN);
          break;
        default:
          throw new UnsupportedOperationException("Unrecognized action: " + event);
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
   * @return true if valid, false otherwise.
   */
  private boolean isStartCommandWithActionValid(Intent intent) {
    if (intent == null) {
      Timber.w("Intent is null");
      return false;
    }

    String action = intent.getAction();
    if (action == null || action.isEmpty()) {
      Timber.w("Intent contains no action.");
      return false;
    }

    return true;
  }
}
