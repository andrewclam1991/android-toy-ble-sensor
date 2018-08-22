package com.andrewclam.weatherclient.service.scanner;

import android.support.annotation.NonNull;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Controls business logics
 * Uses State Pattern to handle states.
 * Uses Strategy Pattern to handle runtime compat issues.
 */
@Singleton
class ScannerController implements ScannerContract.Controller {

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  @NonNull
  private final ScannerContract.State mIdleState;

  @NonNull
  private final ScannerContract.State mActiveState;

  @NonNull
  private ScannerContract.State mCurrentState;

  @Nullable
  private ScannerContract.Service mService;

  @Inject
  ScannerController(@NonNull @Idle ScannerContract.State idleState,
                    @NonNull @Active ScannerContract.State activeState) {
    mIdleState = idleState;
    mActiveState = activeState;
    mCurrentState = mIdleState;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void setService(@NonNull ScannerContract.Service service) {
    mService = service;
  }

  @Override
  public void dropService() {
    mService = null;
    mCompositeDisposable.clear();
  }

  @Override
  public void startScan() {
    mCurrentState.startScan();
  }

  @Override
  public void stopScan() {
    mCurrentState.stopScan();
  }

  @Override
  public void setCurrentState(@NonNull ScannerContract.State state) {
    Timber.d("Current state: %s1, transitioning to state: %s2",
        mCurrentState.getClass().getSimpleName(),
        state.getClass().getSimpleName());

    mCurrentState = state;

  }

  @NonNull
  @Override
  public ScannerContract.State getIdleState() {
    return mIdleState;
  }

  @NonNull
  @Override
  public ScannerContract.State getActiveState() {
    return mActiveState;
  }

//  /**
//   * Encapsulate how scanner should behave when in {@link Idle}
//   */
//  private class Idle implements ScannerContract.State {
//
//    @NonNull
//    private final ScannerContract.Context mContext;
//
//    @NonNull
//    private final ScannerContract.Producer mProducer;
//
//    @NonNull
//    private final BaseSchedulerProvider mSchedulerProvider;
//
//    @NonNull
//    private final CompositeDisposable mCompositeDisposable;
//
//    Idle(@NonNull ScannerContract.Context context,
//              @NonNull ScannerContract.Producer producer,
//              @NonNull BaseSchedulerProvider schedulerProvider) {
//      mContext = context;
//      mProducer = producer;
//      mSchedulerProvider = schedulerProvider;
//      mCompositeDisposable = new CompositeDisposable();
//    }
//
//    @Override
//    public void startScan() {
//      // TODO implement legal, scanner is idle
//      Disposable disposable = mProducer.start()
//          .flatMapCompletable(mRepository::add)
//          .subscribeOn(mSchedulerProvider.io())
//          .observeOn(mSchedulerProvider.ui())
//          .subscribe();
//
//      mCompositeDisposable.add(disposable);
//
//      Timber.d("start started");
//      if (mService != null) {
//        mService.setScanningInProgress(true);
//      }
//      mContext.setCurrentState(mActiveState);
//    }
//
//    @Override
//    public void stopScan() {
//      // no implementation, illegal state, scanner is idle
//      Timber.d("Scan is already idle, stopScan() request ignored.");
//    }
//  }
//
//  /**
//   * Encapsulate how scanner should behave when in {@link Active}
//   */
//  private class Active implements ScannerContract.State {
//
//    @NonNull
//    private final ScannerContract.Context mContext;
//
//    Active(@NonNull ScannerContract.Context context) {
//      mContext = context;
//    }
//
//    @Override
//    public void startScan() {
//      // No implementation, illegal state, scanner is in progress
//      Timber.d("Scan currently in progress, startScan() request ignored.");
//    }
//
//    @Override
//    public void stopScan() {
//      // TODO implement legal, scanner is active
//      if (mService != null) {
//        mService.setScanningInProgress(false);
//      }
//      Timber.d("Scan stopped");
//      mContext.setCurrentState(mIdleState);
//    }
//  }
}
