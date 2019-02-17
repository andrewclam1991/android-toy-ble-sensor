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

package com.andrewclam.weatherclient.view.scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewclam.weatherclient.R;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter class responsible for adapting model data to View and UI classes
 */
public class ScannerRvAdapter extends RecyclerView.Adapter<ScannerRvAdapter.ViewHolder> {

  @NonNull
  private final ScannerViewContract.ListPresenter mListPresenter;

  @Inject
  ScannerRvAdapter(@NonNull ScannerViewContract.ListPresenter listPresenter) {
    mListPresenter = listPresenter;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
    // Get the layout inflater (required to inflate the itemView)
    Context context = parent.getContext(); // Context required by the layoutInflater
    LayoutInflater inflater = LayoutInflater.from(context);

    int layoutResId = R.layout.list_item_ble_device; // item layout style file
    boolean shouldAttachToParentImmediately = false; // optional parameter to indicate exactly what it says

    @SuppressWarnings("ConstantConditions") final View view = inflater.inflate(layoutResId, parent, shouldAttachToParentImmediately);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    mListPresenter.onAdapterBindViewHolder(viewHolder, i);
  }

  @Override
  public int getItemCount() {
    return mListPresenter.onAdapterRequestItemCount();
  }

  final class ViewHolder extends RecyclerView.ViewHolder implements ScannerViewContract.ViewHolder {

    @NonNull
    private final TextView mDeviceNameTv;

    @NonNull
    private final TextView mDeviceMacAddressTv;

    @NonNull
    private final TextView mDeviceSignalTv;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      mDeviceNameTv = itemView.findViewById(R.id.item_device_name_tv);
      mDeviceMacAddressTv = itemView.findViewById(R.id.item_device_mac_address_tv);
      mDeviceSignalTv = itemView.findViewById(R.id.item_device_signal_tv);
    }

    @Override
    public void showDeviceMacAddress(@NonNull String macAddress) {
      mDeviceMacAddressTv.setText(macAddress);
    }

    @Override
    public void showDeviceSignalStrength(@NonNull String signalStrength) {
      mDeviceSignalTv.setText(signalStrength);
    }

    @Override
    public void showDeviceName(@NonNull String deviceName) {
      mDeviceNameTv.setText(deviceName);
    }
  }
}
