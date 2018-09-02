package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.data.source.Repo;
import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.model.ScannerState;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import timber.log.Timber;

/**
 * Controls business logics
 * Uses State Pattern to handle states.
 * Uses Strategy Pattern to handle runtime compat issues.
 */
@ServiceScoped
class ScannerController implements ScannerContract.Controller {

  @Nonnull
  private final ScannerContract.State mIdleState;

  @Nonnull
  private final ScannerContract.State mActiveState;

  @Nullable
  private ScannerContract.Service mService;

  @Nonnull
  private ScannerContract.State mCurrentState;

  @Inject
  ScannerController(@Nonnull ScannerContract.Producer producer,
                    @Nonnull @Repo PeripheralsDataSource repository,
                    @Nonnull @Repo StateSource<ScannerState> stateRepository,
                    @Nonnull BaseSchedulerProvider schedulerProvider) {
    mIdleState = new ScannerStateIdle(this, producer, repository, stateRepository, schedulerProvider);
    mActiveState = new ScannerStateActive(this, producer, schedulerProvider);
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
    Timber.d("Current state: %s, transitioning to state: %s",
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

  @Override
  public void cleanup() {
    mCurrentState.cleanup();
  }
}
