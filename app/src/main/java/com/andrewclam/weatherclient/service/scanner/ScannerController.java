package com.andrewclam.weatherclient.service.scanner;

import android.support.annotation.NonNull;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Controls business logics
 * Uses State Pattern to handle states.
 * Uses Strategy Pattern to handle runtime compat issues.
 */
@Singleton
class ScannerController implements ScannerContract.Controller {

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
  }

  @Override
  public void setService(@NonNull ScannerContract.Service service) {
    mService = service;
  }

  @Override
  public void dropService() {
    mService = null;
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
}
