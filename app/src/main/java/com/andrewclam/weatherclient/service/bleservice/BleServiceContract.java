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
 * com.andrewclam.weatherclient.service.bleservice.BleServiceContract
 */

package com.andrewclam.weatherclient.service.bleservice;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.model.Sensor;
import com.andrewclam.weatherclient.service.BaseController;
import com.andrewclam.weatherclient.service.BaseService;
import com.andrewclam.weatherclient.view.BaseView;

public interface BleServiceContract {

  interface BleServiceView extends BaseView {

    void checkBleSettings();

    void checkBlePermissions();

  }

  interface Service extends ServiceState, BaseService<BleServiceView> {

    void postNotification();

    void postNotificationUpdate();

  }

  interface ServiceState {

    void connect(BluetoothDevice device);

    void disconnect(BluetoothDevice device);

  }

  interface Controller extends BaseController<Service> {

    void sendCommand();

    void onCommandReceived();

    void onCommandFailed();

    void onDataReceived(@NonNull Sensor Sensor);

    void onInvalidDataReceived(@NonNull Throwable throwable);

  }

}
