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
 * com.andrewclam.weatherclient.service.bleservice.BleService
 */

package com.andrewclam.weatherclient.service.bleservice;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import dagger.android.DaggerService;

public class BleService extends DaggerService implements BleServiceContract.Service {

  @NonNull
  private Map<String, BleServiceContract.View> mObservers;

  public BleService() {
    mObservers = new LinkedHashMap<>(0);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void postNotification() {

  }

  @Override
  public void postNotificationUpdate() {

  }

  @NonNull
  @Override
  public String addView(@NonNull BleServiceContract.View view) {
    String viewTag = UUID.randomUUID().toString();
    mObservers.put(viewTag, view);
    return viewTag;
  }

  @Override
  public void dropView(@NonNull String viewTag) {
    if (mObservers.isEmpty() || !mObservers.containsKey(viewTag)) {
      return; // no observers
    }
    mObservers.remove(viewTag);
  }

  @Override
  public void startService() {

  }

  @Override
  public void stopService() {
    cleanUp();
    stopForeground(true);
    stopSelf();
  }

  @Override
  public void connect(BluetoothDevice device) {

  }

  @Override
  public void disconnect(BluetoothDevice device) {

  }

  private void cleanUp() {
    mObservers.clear();
  }
}
