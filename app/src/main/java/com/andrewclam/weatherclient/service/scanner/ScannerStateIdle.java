package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.model.ScannerState;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import org.reactivestreams.Subscription;

import javax.annotation.Nonnull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Internal state of a {@link ScannerContract.Context},
 * implements behaviors when it is idle
 */
@ServiceScoped
final class ScannerStateIdle implements ScannerContract.State {

  @Nonnull
  private final ScannerContract.Context mContext;

  @Nonnull
  private final ScannerContract.Producer mProducer;

  @Nonnull
  private final PeripheralsDataSource mRepository;

  @Nonnull
  private final StateSource<ScannerState> mStateRepository;

  @Nonnull
  private final BaseSchedulerProvider mSchedulerProvider;

  @Nonnull
  private final CompositeDisposable mCompositeDisposable;

  // Defines time span to actively scan for device
  private static final long SCAN_PERIOD_SECONDS = 10;

  ScannerStateIdle(@Nonnull ScannerContract.Context context,
                   @Nonnull ScannerContract.Producer producer,
                   @Nonnull PeripheralsDataSource repository,
                   @Nonnull StateSource<ScannerState> stateRepository,
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
        .doOnSubscribe(this::handleOnScanStarted)
        .doOnTerminate(this::handleOnScanTerminated)
        .subscribeOn(mSchedulerProvider.io())
        .forEach(mRepository::add);

    mCompositeDisposable.add(disposable);
  }

  @Override
  public void stopScan() {
    // no implementation, illegal state, scanner is idle
    Timber.d("Scan is already idle, stopScan() request ignored.");
  }

  private void handleOnScanStarted(@Nonnull Subscription subscription) {
    Timber.d("scan started");
    mContext.setCurrentState(mContext.getActiveState());

    ScannerState state = new ScannerState();
    state.setUid(String.valueOf(this.hashCode()));
    state.setActive(true);

    Disposable disposable = mStateRepository.set(state)
        .subscribeOn(mSchedulerProvider.io())
        .subscribe();

    mCompositeDisposable.add(disposable);
  }

  private void handleOnScanTerminated() {
    Timber.d("scan terminated");
    mContext.setCurrentState(mContext.getIdleState());

    ScannerState state = new ScannerState();
    state.setUid(String.valueOf(this.hashCode()));
    state.setActive(false);

    Disposable disposable = mStateRepository.set(state)
        .subscribeOn(mSchedulerProvider.io())
        .subscribe();

    mCompositeDisposable.add(disposable);
  }

  @Override
  public void cleanup() {
    mCompositeDisposable.clear();
  }
}
