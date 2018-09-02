package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.model.ScannerState;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.Observable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
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
    Timber.d("scan started");
    mContext.setCurrentState(mContext.getActiveState());

    ScannerState state = new ScannerState();
    state.setUid(String.valueOf(this.hashCode()));
    state.setActive(true);

    Disposable disposable = mStateRepository.set(state)
        .andThen(mProducer.start())
        .doOnNext(mRepository::add)
        .doOnTerminate(this::handleOnScanTerminated)
        .subscribeOn(mSchedulerProvider.io())
        .subscribe();

    mCompositeDisposable.add(disposable);
  }

  @Override
  public void stopScan() {
    // no implementation, illegal state, scanner is idle
    Timber.d("Scan is already idle, stopScan() request ignored.");
  }

  private void handleOnScanTerminated() {
    Timber.d("scan terminated");
    mContext.setCurrentState(mContext.getIdleState());

    ScannerState state = new ScannerState();
    state.setUid(String.valueOf(this.hashCode()));
    state.setActive(false);

    Disposable disposable = mProducer.stop()
        .andThen(mStateRepository.set(state))
        .subscribeOn(mSchedulerProvider.io())
        .subscribe();

    mCompositeDisposable.add(disposable);
  }

  @Override
  public void cleanup() {
    mCompositeDisposable.clear();
  }
}
