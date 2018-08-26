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

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ScannerServiceModule {

  @Nonnull
  @Singleton
  @Provides
  static BluetoothAdapter providesBluetoothAdapter(@Nonnull Application context){
    final BluetoothManager bluetoothManager =
        (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    BluetoothAdapter adapter;
    if (bluetoothManager != null) {
      adapter = bluetoothManager.getAdapter();
    } else{
      throw new UnsupportedOperationException("Unable to get device bluetooth adapter");
    }
    return adapter;
  }

  @Nonnull
  @Singleton
  @Binds
  @ScannerContract.State.Active
  abstract ScannerContract.State providesActiveState(@Nonnull ScannerStateActive activeState);

  @Nonnull
  @Singleton
  @Binds
  @ScannerContract.State.Idle
  abstract ScannerContract.State providesIdleState(@Nonnull ScannerStateIdle idleState);
}
