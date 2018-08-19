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
 * com.andrewclam.toyblesensor.BleSensorApplication
 */

package com.andrewclam.toyblesensor;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.andrewclam.toyblesensor.data.source.sensor.SensorsRepository;
import com.andrewclam.toyblesensor.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

/**
 * We create a custom {@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
public class BleSensorApplication extends DaggerApplication {
  @Inject
  SensorsRepository mSensorsRepository;

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    return DaggerAppComponent.builder().application(this).build();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  /**
   * Our Espresso tests need to be able to get an instance of the {@link SensorsRepository}
   * so that we can delete all tasks before running each test
   */
  @VisibleForTesting
  public SensorsRepository getSensorsRepository() {
    return mSensorsRepository;
  }
}
