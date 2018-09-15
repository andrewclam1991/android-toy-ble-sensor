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
 * ScannerViewContract.java
 *
 */

package com.andrewclam.weatherclient.view.scanner;

import com.andrewclam.weatherclient.view.BasePresenter;
import com.andrewclam.weatherclient.view.BaseView;

public interface ScannerViewContract {

  interface View extends BaseView {
    void showScannerInProgress(boolean isVisible);

    void onUserStartScan();

    void onUserStopScan();
  }

  interface Presenter extends BasePresenter<View> {
    void loadScannerState();

    void loadPeripherals();
  }
}
