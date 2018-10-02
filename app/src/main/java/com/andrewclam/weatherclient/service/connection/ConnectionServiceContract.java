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

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.model.Sensor;
import com.andrewclam.weatherclient.service.BaseController;
import com.andrewclam.weatherclient.service.BaseService;
import com.andrewclam.weatherclient.view.BaseView;

/**
 * Responsible for starting, maintaining and ending the connection
 * between {@link com.andrewclam.weatherclient.model.Peripheral} devices
 * and the central device.
 */
public interface ConnectionServiceContract {

  interface View extends BaseView {

    void checkBluetoothAdapterSettings();

    void checkBluetoothPermissions();

  }

  interface Service extends ServiceState, BaseService {

    void postNotification();

    void postNotificationUpdate();

  }

  interface ServiceState {

    void connect(Peripheral peripheral);

    void reconnect(Peripheral peripheral);

    void disconnect(Peripheral peripheral);

  }

  interface Controller extends BaseController {

    void sendCommand();

    void onCommandReceived();

    void onCommandFailed();

    void onDataReceived(@NonNull Sensor Sensor);

    void onInvalidDataReceived(@NonNull Throwable throwable);

  }

}
