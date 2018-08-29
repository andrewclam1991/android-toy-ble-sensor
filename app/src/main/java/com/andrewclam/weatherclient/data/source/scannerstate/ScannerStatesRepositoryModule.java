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
 * com.andrewclam.weatherclient.data.source.peripheral.PeripheralsRepositoryModule
 */

package com.andrewclam.weatherclient.data.source.scannerstate;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.data.source.Repo;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * This is used by Dagger to inject the required arguments into the {@link ScannerStatesCacheDataSource}.
 */
@Module
public abstract class ScannerStatesRepositoryModule {

  @NonNull
  @Singleton
  @Binds
  @Repo
  abstract ScannerStatesCacheDataSource providesRepository(@NonNull ScannerStatesCacheDataSource repository);

}
