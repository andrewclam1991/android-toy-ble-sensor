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

package com.andrewclam.weatherclient.scheduler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Provides different types of schedulers.
 */
public class SchedulerProvider implements BaseSchedulerProvider {

  @Nullable
  private static SchedulerProvider INSTANCE;

  // Prevent direct instantiation.
  private SchedulerProvider() {
  }

  @NonNull
  public static synchronized SchedulerProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SchedulerProvider();
    }
    return INSTANCE;
  }

  @Override
  @NonNull
  public Scheduler computation() {
    return Schedulers.computation();
  }

  @Override
  @NonNull
  public Scheduler io() {
    return Schedulers.io();
  }

  @Override
  @NonNull
  public Scheduler ui() {
    return AndroidSchedulers.mainThread();
  }
}
