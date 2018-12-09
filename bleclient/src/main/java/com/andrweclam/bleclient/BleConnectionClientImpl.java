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

package com.andrweclam.bleclient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Multi-client implementation
 *
 * use case
 * - sensor capable of
 * -- sensing temperature
 * -- sensing humidity
 * -- sensing n data types (characteristic)
 * -- receive command as write characteristic to:
 * --- start sensing n data types
 *
 * Server requirement
 * - sensor broadcasts a service, that contains
 * - n READ characteristics,
 * - m WRITE characteristics
 *
 * - eg temperature is READ, humidity is READ, notify on characteristic changed
 * - eg get_temperature is WRITE, get_humidity is WRITE, allows server to start streaming
 *
 * Client requirement:
 * - Having n clients to handle each gatt state callbacks
 *
 *
 */
class BleConnectionClientImpl extends BluetoothGattCallback implements BleConnectionClient {

  private final static UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902);
  private final static byte COMMAND_GET_TEMPERATURE_UPDATE = 0x1A;

  @NonNull
  private final Context mContext;

  @NonNull
  private final BluetoothAdapter mAdapter;

  @NonNull
  private Map<String, BluetoothGatt> mGatts;

  private Collection<ServiceClient> mServiceClients;


  @Inject
  BleConnectionClientImpl(@NonNull Context context,
                          @NonNull BluetoothAdapter adapter) {
    mContext = context;
    mAdapter = adapter;
    mGatts = new LinkedHashMap<>();
  }

  @NonNull
  @Override
  public BluetoothDevice getBleDevice(@NonNull String macAddress) {
    if (BluetoothAdapter.checkBluetoothAddress(macAddress)) {
      return mAdapter.getRemoteDevice(macAddress);
    } else {
      throw new IllegalArgumentException("Invalid mac address");
    }
  }

  @Override
  public void connect(@NonNull BluetoothDevice device) {
    String macAddress = device.getAddress();
    BluetoothGatt gatt = device.connectGatt(mContext, false, this);
    mGatts.put(macAddress, gatt);
  }

  @Override
  public void disconnect(@NonNull BluetoothDevice device) {
    String macAddress = device.getAddress();
    if (mGatts.containsKey(macAddress)) {
      BluetoothGatt gatt = mGatts.get(macAddress);
      if (gatt != null) {
        gatt.disconnect();
        gatt.close();
      }
      mGatts.remove(macAddress);
    }
  }

  @Override
  public void disconnectAll() {
    for (BluetoothGatt gatt : mGatts.values()) {
      if (gatt != null) {
        gatt.disconnect();
        gatt.close();
      }
    }
    mGatts.clear();
  }

  @Override
  public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
    super.onConnectionStateChange(gatt, status, newState);
    switch (newState){
      case BluetoothProfile.STATE_CONNECTED:
        gatt.discoverServices();
        break;
      case BluetoothProfile.STATE_DISCONNECTED:
        //Timber.i("BLE Disconnected");
        break;
    }
  }

  @Override
  public void onServicesDiscovered(BluetoothGatt gatt, int status) {
    super.onServicesDiscovered(gatt, status);
    for (ServiceClient client : mServiceClients){
      client.onServicesDiscovered(gatt,status);
    }

//    gatt.getServices();
////
////    switch (status) {
////      case BluetoothGatt.GATT_SUCCESS:
////
////
////        // Write to local to get characteristic notification
////        BluetoothGattCharacteristic characteristic = gatt.getService(HEART_RATE_SERVICE_UUID)
////            .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID);
////        gatt.setCharacteristicNotification(characteristic, true);
////
////        // Write to remote descriptor to enables the particular notification mode
////        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
////        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
////        gatt.writeDescriptor(descriptor);
////
////        break;
////      case BluetoothGatt.GATT_FAILURE:
////        break;
////    }
  }

  @Override
  public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
    super.onCharacteristicRead(gatt, characteristic, status);
  }

  /**
   * Callback when a char is written to the remote
   * @param gatt
   * @param characteristic
   * @param status
   */
  @Override
  public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
    super.onCharacteristicWrite(gatt, characteristic, status);
    for (BluetoothGattCallback callback : mServiceClients){
      callback.onServicesDiscovered(gatt,status);
    }
//    byte command = characteristic.getValue()[0];
//    if (status == BluetoothGatt.GATT_SUCCESS) {
//      switch (command){
//        case COMMAND_GET_TEMPERATURE_UPDATE:
//          mTemperatureUpdateProcessor.onNext(0.0d);
//          break;
//      }
//    }else if (status == BluetoothGatt.GATT_FAILURE) {
//      switch (command){
//        case COMMAND_GET_TEMPERATURE_UPDATE:
//          mTemperatureUpdateProcessor.onError(new Throwable("GATT_FAILURE"));
//          break;
//      }
//    }
  }

  @Override
  public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
    super.onCharacteristicChanged(gatt, characteristic);
  }

  @Override
  public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    super.onDescriptorRead(gatt, descriptor, status);
  }

  @Override
  public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    super.onDescriptorWrite(gatt, descriptor, status);
  }

  private static UUID convertFromInteger(int i) {
    final long MSB = 0x0000000000001000L;
    final long LSB = 0x800000805f9b34fbL;
    return new UUID(MSB | ((long) i << 32), LSB);
  }

}
