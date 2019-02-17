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

import com.andrewclam.weatherclient.view.BasePresenter;
import com.andrewclam.weatherclient.view.BaseView;

import androidx.annotation.NonNull;

public interface ScannerViewContract {

  interface View extends BaseView {

    /**
     * Notifies View when scanner is in progress
     *
     * @param isVisible whether the loading indicator should be visible
     */
    void showScannerInProgress(boolean isVisible);

    /**
     * Notifies when data is ready to be shown.
     */
    void notifyDataSetChanged();

    void notifyItemInserted(int position);
  }

  interface ViewHolder {

    void showDeviceMacAddress(@NonNull String macAddress);

    void showDeviceSignalStrength(@NonNull String signalStrength);

    void showDeviceName(@NonNull String deviceName);

  }

  interface Presenter extends BasePresenter<View> {
    /**
     * Subscribes to the scanner state updates
     */
    void loadScannerState();

    /**
     * Subscribes to a list of discovered {@link com.andrewclam.weatherclient.model.Peripheral}
     *
     * @param forceRefresh whether to clear cache and refresh
     */
    void loadPeripherals(boolean forceRefresh);
  }

  interface ListPresenter {

    void onAdapterItemClicked(int adapterPosition);

    void onAdapterBindViewHolder(@NonNull ViewHolder viewHolder, int adapterPosition);

    int onAdapterRequestItemCount();

  }
}
