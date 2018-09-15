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
 * BaseModel.java
 *
 */

package com.andrewclam.weatherclient.model;

import android.support.annotation.NonNull;

public interface BaseModel {
  /**
   * Sets the unique id
   *
   * @param uid unique id of a data model
   */
  void setUid(@NonNull String uid);

  /**
   * @return unique id of a data model
   */
  @NonNull
  String getUid();
}
