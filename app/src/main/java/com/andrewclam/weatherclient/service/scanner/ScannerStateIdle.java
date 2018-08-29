package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.data.source.Repo;
import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.data.source.scannerstate.ScannerStatesDataSource;
import com.andrewclam.weatherclient.model.ScannerState;
import com.andrewclam.weatherclient.schedulers.BaseSchedulerProvider;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Internal state of a {@link ScannerContract.Context},
 * implements behaviors when it is in idle
 */
@Singleton
final class ScannerStateIdle implements ScannerContract.State {

  @NonNull
  private final ScannerContract.Context mContext;

  @NonNull
  private final ScannerContract.Producer mProducer;

  @NonNull
  private final PeripheralsDataSource mRepository;

  @Nonnull
  private final ScannerStatesDataSource mStateRepository;

  @NonNull
  private final BaseSchedulerProvider mSchedulerProvider;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  // Defines time span to actively scan for device
  private static final long SCAN_PERIOD_SECONDS = 10;

  @Inject
  ScannerStateIdle(@Nonnull ScannerContract.Context context,
                   @Nonnull ScannerContract.Producer producer,
                   @Nonnull @Repo PeripheralsDataSource repository,
                   @Nonnull @Repo ScannerStatesDataSource stateRepository,
                   @Nonnull BaseSchedulerProvider schedulerProvider) {
    mContext = context;
    mProducer = producer;
    mRepository = repository;
    mStateRepository = stateRepository;
    mSchedulerProvider = schedulerProvider;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void startScan() {
    Disposable disposable = mProducer.start()
        .delay(SCAN_PERIOD_SECONDS, TimeUnit.SECONDS)
        .doOnSubscribe(this::handleOnScanStarted)
        .doOnTerminate(this::handleOnScanTerminated)
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

  private void handleOnScanStarted(@Nonnull Subscription subscription){
    Timber.d("scan started");
    mContext.setCurrentState(mContext.getActiveState());

    ScannerState state = new ScannerState();
    state.setUid(String.valueOf(this.hashCode()));
    state.setActive(true);
    Disposable disposable = mStateRepository.update(state)
        .subscribeOn(mSchedulerProvider.io())
        .subscribe();

    mCompositeDisposable.add(disposable);
  }

  private void handleOnScanTerminated(){
    Timber.d("scan terminated");
    mContext.setCurrentState(mContext.getIdleState());

    ScannerState state = new ScannerState();
    state.setUid(String.valueOf(this.hashCode()));
    state.setActive(false);
    Disposable disposable = mStateRepository.update(state)
        .subscribeOn(mSchedulerProvider.io())
        .subscribe();

    mCompositeDisposable.add(disposable);
  }
}
