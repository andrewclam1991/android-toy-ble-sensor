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
 * WeatherClientApplication.java
 *
 */

package com.andrewclam.weatherclient;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.multidex.MultiDex;

import com.andrewclam.weatherclient.data.source.sensor.SensorsRepository;
import com.andrewclam.weatherclient.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

/**
 * We create a custom {@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
public class WeatherClientApplication extends DaggerApplication {
  @Inject
  SensorsRepository mSensorsRepository;

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    return DaggerAppComponent.builder().application(this).build();
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree() {
        @Override
        protected String createStackElementTag(@NonNull StackTraceElement element) {
          return String.format("(%s:%s)#%s",
              element.getFileName(),
              element.getLineNumber(),
              element.getMethodName());
        }
      });
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
