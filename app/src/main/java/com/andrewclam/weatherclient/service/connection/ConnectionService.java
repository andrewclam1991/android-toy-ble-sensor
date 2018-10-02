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

package com.andrewclam.weatherclient.service.connection;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.andrewclam.weatherclient.model.Peripheral;

import dagger.android.DaggerService;

public class ConnectionService extends DaggerService implements ConnectionServiceContract.Service {

  public ConnectionService() {

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
  public void connect(Peripheral peripheral) {

  }

  @Override
  public void reconnect(Peripheral peripheral) {

  }

  @Override
  public void disconnect(Peripheral peripheral) {

  }

  private void cleanUp() {

  }
}
