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

package com.andrewclam.weatherclient.data.state;

import com.andrewclam.weatherclient.model.BaseModel;

import javax.annotation.Nonnull;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Reactive and thread-safe state get and put interface
 */
public interface StateSource<S extends BaseModel> {
  @Nonnull
  Flowable<S> get();

  @Nonnull
  Completable set(@Nonnull S source);
}
