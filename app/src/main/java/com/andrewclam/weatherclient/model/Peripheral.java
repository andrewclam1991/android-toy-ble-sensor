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

package com.andrewclam.weatherclient.model;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Models a bluetooth peripheral device data
 */
public class Peripheral implements BaseModel {
  /**
   * UUID of the peripheral bluetooth device
   */
  private String uid;

  /**
   * The peripheral bluetooth device
   */
  private BluetoothDevice bluetoothDevice;

  /**
   * Remembers the relative received signal strength in
   * the wireless environment, higher rssi means the stronger the signal
   */
  private double rssi;

  /**
   * Remembers the content of the advertisement record offered by the remote device.
   */
  private byte[] scanRecord;

  @Override
  public void setUid(@NonNull String uid) {
    this.uid = uid;
  }

  @NonNull
  @Override
  public String getUid() {
    return uid;
  }

  @NonNull
  public BluetoothDevice getBluetoothDevice() {
    return bluetoothDevice;
  }

  public void setBluetoothDevice(@NonNull BluetoothDevice bluetoothDevice) {
    this.bluetoothDevice = bluetoothDevice;
  }

  public double getRssi() {
    return rssi;
  }

  public void setRssi(double rssi) {
    this.rssi = rssi;
  }

  public byte[] getScanRecord() {
    return scanRecord;
  }

  public void setScanRecord(byte[] scanRecord) {
    this.scanRecord = scanRecord;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + uid.hashCode();
    return hash;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Peripheral)) {
      return false;
    }

    if (obj == this) {
      return true;
    }

    Peripheral peripheralRight = (Peripheral) obj;
    String addressRight = peripheralRight.getBluetoothDevice().getAddress();
    String addressLeft = this.getBluetoothDevice().getAddress();
    boolean isSameAddress = addressLeft.equals(addressRight);
    return isSameAddress;
  }
}
