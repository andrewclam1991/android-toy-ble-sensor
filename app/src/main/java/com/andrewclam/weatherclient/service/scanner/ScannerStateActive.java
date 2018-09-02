package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import javax.annotation.Nonnull;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Internal state of a {@link ScannerContract.Context},
 * implements behaviors when it is active
 */
@ServiceScoped
final class ScannerStateActive implements ScannerContract.State {

  @Nonnull
  private final ScannerContract.Context mContext;

  @Nonnull
  private final ScannerContract.Producer mProducer;

  @Nonnull
  private final BaseSchedulerProvider mSchedulerProvider;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  ScannerStateActive(@Nonnull ScannerContract.Context context,
                     @Nonnull ScannerContract.Producer producer,
                     @Nonnull BaseSchedulerProvider schedulerProvider) {
    mContext = context;
    mProducer = producer;
    mSchedulerProvider = schedulerProvider;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void startScan() {
    // No implementation, illegal state, scanner is in progress
    Timber.d("Scan currently in progress, startScan() request ignored.");
  }

  @Override
  public void stopScan() {
    Timber.d("scan stopping...");
    Disposable disposable = mProducer.stop()
        .subscribeOn(mSchedulerProvider.io())
        .subscribe(this::handleOnScanStopped);

    mCompositeDisposable.add(disposable);
  }

  private void handleOnScanStopped() {
    Timber.d("scan stopped");
    mContext.setCurrentState(mContext.getIdleState());
  }

  @Override
  public void cleanup() {
    mCompositeDisposable.clear();
  }
}
