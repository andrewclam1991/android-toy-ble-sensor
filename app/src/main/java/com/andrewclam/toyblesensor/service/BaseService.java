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
 * com.andrewclam.toyblesensor.service.BaseService
 */

package com.andrewclam.toyblesensor.service;

import android.support.annotation.NonNull;

import com.andrewclam.toyblesensor.view.BaseView;

public interface BaseService<V extends BaseView> {
  /**
   * Add observer {@link BaseView} with view tag
   *
   * @param view instance of a {@link BaseView}
   * @return the instance unique tag that identifies the {@link BaseView}
   */
  @NonNull
  String addView(@NonNull V view);

  /**
   * Removes an observer {@link BaseView} base on its unique tag
   *
   * @param viewTag the instance unique tag that identifies the {@link BaseView}
   */
  void dropView(@NonNull String viewTag);

  /**
   * Command to start this {@link BaseService<V>}
   */
  void startService();

  /**
   * Command to terminate this {@link BaseService<V>}
   */
  void stopService();
}
