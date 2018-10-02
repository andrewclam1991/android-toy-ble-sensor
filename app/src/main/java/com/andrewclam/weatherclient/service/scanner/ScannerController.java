/*
 * Copyright 2018 Andrew Chi Heng Lam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.data.source.Repo;
import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.model.ScannerState;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import javax.annotation.Nonnull;
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

  @Nonnull
  private ScannerContract.State mCurrentState;

  @Inject
  ScannerController(@Nonnull ScannerContract.Producer producer,
                    @Nonnull @Repo PeripheralsDataSource repository,
                    @Nonnull @Repo StateSource<ScannerState> stateRepository,
                    @Nonnull BaseSchedulerProvider schedulerProvider) {
    mIdleState = new ScannerStateIdle(this, producer, repository, stateRepository, schedulerProvider);
    mActiveState = new ScannerStateActive(this, producer, stateRepository, schedulerProvider);
    mCurrentState = mIdleState;
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
    getActiveState().cleanup();
    getIdleState().cleanup();
  }
}
