package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.di.ServiceScoped;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Controls business logics
 * Uses State Pattern to handle states.
 * Uses Strategy Pattern to handle runtime compat issues.
 */
@ServiceScoped
@Singleton
class ScannerController implements ScannerContract.Controller {

  @Inject
  ScannerContract.State mIdleState;

  @Inject
  ScannerContract.State mActiveState;

  @Nonnull
  private ScannerContract.State mCurrentState;

  @Nullable
  private ScannerContract.Service mService;

  @Inject
  ScannerController() {
    mCurrentState = mIdleState;
  }

  @Override
  public void setService(@Nonnull ScannerContract.Service service) {
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
  public void setCurrentState(@Nonnull ScannerContract.State state) {
    Timber.d("Current state: %s1, transitioning to state: %s2",
        mCurrentState.getClass().getSimpleName(),
        state.getClass().getSimpleName());
    mCurrentState = state;
  }

  @Nonnull
  @Override
  public ScannerContract.State getIdleState() {
    return mIdleState;
  }

  @Nonnull
  @Override
  public ScannerContract.State getActiveState() {
    return mActiveState;
  }
}
