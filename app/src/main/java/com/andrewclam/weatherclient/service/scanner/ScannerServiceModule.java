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
 * com.andrewclam.weatherclient.di.modules.SharedPreferenceModule
 */

package com.andrewclam.weatherclient.service.scanner;

import android.bluetooth.BluetoothAdapter;

import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.view.scanner.ScannerActivity;

import java.util.Scanner;

import javax.annotation.Nonnull;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ScannerServiceModule {

  @Nonnull
  @ServiceScoped
  @Binds
  abstract ScannerContract.Controller providesController(@Nonnull ScannerController scannerController);

  @Nonnull
  @ServiceScoped
  @Binds
  abstract ScannerContract.Context providesContext(@Nonnull ScannerController scannerController);

  @Nonnull
  @ServiceScoped
  @Binds
  abstract ScannerContract.Producer providesProducer(@Nonnull PeripheralProducer peripheralProducer);

  @Nonnull
  @ServiceScoped
  @Binds
  @ScannerContract.State.Active
  abstract ScannerContract.State providesActiveState(@Nonnull ScannerStateActive activeState);

  @Nonnull
  @ServiceScoped
  @Binds
  @ScannerContract.State.Idle
  abstract ScannerContract.State providesIdleState(@Nonnull ScannerStateIdle idleState);
}
