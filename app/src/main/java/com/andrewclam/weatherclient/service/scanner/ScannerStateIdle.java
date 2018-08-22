package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.data.source.Repo;
import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.schedulers.BaseSchedulerProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Internal state of a {@link ScannerContract.Context},
 * implements behaviors when it is in idle
 */
@Singleton
final class ScannerStateIdle implements ScannerContract.State {

  @Nullable
  private ScannerContract.Context mContext;

  @NonNull
  private final ScannerContract.Producer mProducer;

  @NonNull
  private final PeripheralsDataSource mRepository;

  @NonNull
  private final BaseSchedulerProvider mSchedulerProvider;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  // Defines time span to actively scan for device
  private static final long SCAN_PERIOD_SECONDS = 10;

  @Inject
  ScannerStateIdle(@NonNull ScannerContract.Producer producer,
                   @NonNull @Repo PeripheralsDataSource repository,
                   @NonNull BaseSchedulerProvider schedulerProvider) {
    mProducer = producer;
    mRepository = repository;
    mSchedulerProvider = schedulerProvider;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void startScan() {
    Disposable disposable = mProducer.start()
        .delay(SCAN_PERIOD_SECONDS, TimeUnit.SECONDS)
        .doOnSubscribe(subscription -> {
          Timber.d("scan started");
          mContext.setCurrentState(mContext.getActiveState());
        })
        .doFinally(mCompositeDisposable::clear)
        .subscribeOn(mSchedulerProvider.io())
        .forEach(mRepository::add);

    mCompositeDisposable.add(disposable);
  }

  @Override
  public void stopScan() {
    // no implementation, illegal state, scanner is idle
    Timber.d("Scan is already idle, stopScan() request ignored.");
  }

  @Override
  public void setContext(@NonNull ScannerContract.Context context) {
    mContext = context;
  }

  @Override
  public void dropContext() {
    mContext = null;
    mCompositeDisposable.clear();
  }
}
