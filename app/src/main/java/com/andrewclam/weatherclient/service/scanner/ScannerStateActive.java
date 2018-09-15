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
 *
 * ScannerStateActive.java
 *
 */

package com.andrewclam.weatherclient.service.scanner;

import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import javax.annotation.Nonnull;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Internal state of a {@link ScannerContract.Context},
 * implements behaviors when it is active
 */
@ServiceScoped
final class ScannerStateActive implements ScannerContract.State {

  @Nonnull
  private final ScannerContract.Context mContext;

  @Nonnull
  private final ScannerContract.Producer mProducer;

  @Nonnull
  private final BaseSchedulerProvider mSchedulerProvider;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  ScannerStateActive(@Nonnull ScannerContract.Context context,
                     @Nonnull ScannerContract.Producer producer,
                     @Nonnull BaseSchedulerProvider schedulerProvider) {
    mContext = context;
    mProducer = producer;
    mSchedulerProvider = schedulerProvider;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public void startScan() {
    // No implementation, illegal state, scanner is in progress
    Timber.d("Scan currently in progress, startScan() request ignored.");
  }

  @Override
  public void stopScan() {
    Timber.d("scan stopping...");
    Disposable disposable = mProducer.stop()
        .subscribeOn(mSchedulerProvider.io())
        .subscribe(this::handleOnScanStopped);

    mCompositeDisposable.add(disposable);
  }

  private void handleOnScanStopped() {
    Timber.d("scan stopped");
    mContext.setCurrentState(mContext.getIdleState());
  }

  @Override
  public void cleanup() {
    mCompositeDisposable.clear();
  }
}
