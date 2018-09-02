package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.di.ServiceScoped;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import dagger.Lazy;
import timber.log.Timber;

/**
 * Controls business logics
 * Uses State Pattern to handle states.
 * Uses Strategy Pattern to handle runtime compat issues.
 */
@ServiceScoped
class ScannerController implements ScannerContract.Controller {

  @Inject
  @Idle
  Lazy<ScannerContract.State> mIdleState;

  @Inject
  @Active
  Lazy<ScannerContract.State> mActiveState;

  @Nullable
  private ScannerContract.Service mService;

  @Nullable
  private ScannerContract.State mCurrentState;

  @Inject
  ScannerController() {}

  @Override
  public void setService(@Nonnull ScannerContract.Service service) {
    mCurrentState = mIdleState.get();
    mService = service;
  }

  @Override
  public void dropService() {
    mService = null;
  }

  @Override
  public void startScan() {
    if (mCurrentState != null) {
      mCurrentState.startScan();
    }
  }

  @Override
  public void stopScan() {
    if (mCurrentState != null) {
      mCurrentState.stopScan();
    }
  }

  @Override
  public void setCurrentState(@Nonnull ScannerContract.State state) {
    if (mCurrentState != null) {
      Timber.d("Current state: %s1, transitioning to state: %s2",
          mCurrentState.getClass().getSimpleName(),
          state.getClass().getSimpleName());
    }
    mCurrentState = state;
  }

  @Nonnull
  @Override
  public ScannerContract.State getIdleState() {
    return mIdleState.get();
  }

  @Nonnull
  @Override
  public ScannerContract.State getActiveState() {
    return mActiveState.get();
  }
}
