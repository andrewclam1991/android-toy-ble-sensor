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
 * com.andrewclam.toyblesensor.service.BaseController
 */

package com.andrewclam.toyblesensor.service;

import android.support.annotation.NonNull;

public interface BaseController<S extends BaseService> {
  /**
   * Command to set reference to the {@code Service}
   *
   * @param service instance of {@link BaseService}
   */
  void setService(@NonNull S service);

  /**
   * Command to drop reference of the {@link BaseService}
   * prevents memory leak
   */
  void dropService();
}
