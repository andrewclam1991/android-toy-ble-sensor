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
 * com.andrewclam.weatherclient.service.pairing.PairingService
 */

package com.andrewclam.weatherclient.service.scanner;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import dagger.android.DaggerService;

/**
 * Simple Service class
 * Delegates scanning for ble peripheral devices to its {@link ScannerContract.Controller}
 */
public final class ScannerService extends DaggerService implements ScannerContract.Service {

  @Inject
  ScannerContract.Controller mController;

  private ScannerContract.Authority mAuthority;

  public ScannerService() {
    // Required no-arg constructor
  }

  @Override
  public void addAuthority(@Nonnull ScannerContract.Authority authority) {
    mAuthority = authority;
  }

  @Override
  public void dropAuthority() {
    mAuthority = null;
  }

  @Override
  public void startScan() {
    mController.startScan();
  }

  @Override
  public void stopScan() {
    mController.stopScan();
  }

  @Override
  public void startService() {
    startScan();
  }

  @Override
  public void stopService() {
    stopScan();
    stopForeground(true);
    stopSelf();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
