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

  ScannerStateIdle(@Nonnull ScannerContract.Context context,
                   @Nonnull ScannerContract.Producer producer,
                   @Nonnull @Repo PeripheralsDataSource repository,
                   @Nonnull @Repo StateSource<ScannerState> stateRepository,
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
    mCompositeDisposable.clear();

    Disposable disposable = mStateRepository.set(ScannerState.getActiveState())
        .andThen(mProducer.start())
        .flatMapCompletable(mRepository::add)
        .doOnSubscribe(subscription -> {
          Timber.d("scan started");
          mRepository.refresh();
          mContext.setCurrentState(mContext.getActiveState());
        })
        .subscribeOn(mSchedulerProvider.io())
        .subscribe();

    mCompositeDisposable.add(disposable);
  }

  @Override
  public void stopScan() {
    // no implementation, illegal state, scanner is idle
    Timber.d("Scan is already idle, stopScan() request ignored.");
  }

  @Override
  public void cleanup() {
    mCompositeDisposable.clear();
  }
}
