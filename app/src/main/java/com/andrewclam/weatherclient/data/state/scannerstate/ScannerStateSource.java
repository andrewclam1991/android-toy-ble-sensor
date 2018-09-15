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
 * ScannerStateSource.java
 *
 */

package com.andrewclam.weatherclient.data.state.scannerstate;

import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.model.ScannerState;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

@Singleton
public class ScannerStateSource implements StateSource<ScannerState> {

  @Nonnull
  private final PublishProcessor<ScannerState> mScannerState;

  @Inject
  ScannerStateSource() {
    mScannerState = PublishProcessor.create();
  }

  @Nonnull
  @Override
  public Flowable<ScannerState> get() {
    return mScannerState;
  }

  @Nonnull
  @Override
  public Completable set(@Nonnull ScannerState source) {
    return Completable.create(emitter -> {
      mScannerState.onNext(source);
      emitter.onComplete();
    });
  }
}
