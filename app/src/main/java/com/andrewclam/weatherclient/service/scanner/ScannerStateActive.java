package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.schedulers.BaseSchedulerProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Encapsulate how scanner should behave when in {@link ScannerStateActive}
 */
@Singleton
final class ScannerStateActive implements ScannerContract.State {

  @NonNull
  private final ScannerContract.Context mContext;

  @NonNull
  private final ScannerContract.Producer mProducer;

  @NonNull
  private final BaseSchedulerProvider mSchedulerProvider;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  @Inject
  ScannerStateActive(@Nonnull ScannerContract.Context context,
                     @NonNull ScannerContract.Producer producer,
                     @NonNull BaseSchedulerProvider schedulerProvider) {
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
        .subscribe(() -> {
          Timber.d("scan stopped");
          mContext.setCurrentState(mContext.getIdleState());
        });

    mCompositeDisposable.add(disposable);
  }

}
