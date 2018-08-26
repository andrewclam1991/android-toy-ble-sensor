package com.andrewclam.weatherclient.service.scanner;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ScannerControllerContext implements ScannerContract.Context {

  // TODO possible cyclic dependency between the child state and this context
  @Inject
  ScannerContract.State mIdleState;

  @Inject
  ScannerContract.State mActiveState;

  @NonNull
  private ScannerContract.State mCurrentState;

  @Inject
  ScannerControllerContext() {
    mCurrentState = mIdleState;
  }

  @Override
  public void setCurrentState(ScannerContract.State state) {
    mCurrentState = state;
  }

  @Override
  public ScannerContract.State getIdleState() {
    return mIdleState;
  }

  @Override
  public ScannerContract.State getActiveState() {
    return mActiveState;
  }
}
