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
 * com.andrewclam.weatherclient.view.BasePresenter
 */

package com.andrewclam.weatherclient.view;

import android.support.annotation.NonNull;

/**
 * Base Presenter class
 *
 * @param <V>
 */
public interface BasePresenter<V extends BaseView> {
  /**
   * Add observer {@link BaseView} with view tag
   *
   * @param view instance of a {@link BaseView}
   */
  void addView(@NonNull V view);

  /**
   * Removes an observer {@link BaseView} from
   *
   * @param view instance of a {@link BaseView}
   */
  void dropView(@NonNull V view);
}
