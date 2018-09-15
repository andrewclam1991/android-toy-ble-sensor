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
 * MainContract.java
 *
 */

package com.andrewclam.weatherclient.view.main;

import com.andrewclam.weatherclient.view.BasePresenter;
import com.andrewclam.weatherclient.view.BaseView;

interface MainContract {
  interface View extends BaseView {
    void showScannerUi();
  }

  interface Presenter extends BasePresenter<View> {

  }
}
