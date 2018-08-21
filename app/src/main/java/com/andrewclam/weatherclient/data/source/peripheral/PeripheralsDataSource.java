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
 * com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource
 */

package com.andrewclam.weatherclient.data.source.peripheral;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.data.source.DataSource;
import com.andrewclam.weatherclient.model.Peripheral;

import java.util.List;

import io.reactivex.Flowable;

/**
 * API exposes {@link Peripheral} model specific data source requirements
 */
public interface PeripheralsDataSource extends DataSource<Peripheral> {

  /* Allows for future extensibility */

  /**
   * Allows central device to scan for near by discoverable {@link Peripheral}s
   *
   * @return an observable list of discoverable {@link Peripheral}
   */
  @NonNull
  Flowable<List<Peripheral>> scan();

  /**
   * @return flags whether an instance of scan is currently in progress
   */
  boolean isScanning();
}
