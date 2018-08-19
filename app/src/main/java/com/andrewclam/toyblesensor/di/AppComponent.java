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
 * com.andrewclam.toyblesensor.di.AppComponent
 */

package com.andrewclam.toyblesensor.di;

import android.app.Application;

import com.andrewclam.toyblesensor.BleSensorApplication;
import com.andrewclam.toyblesensor.data.roomdb.AppDatabaseModule;
import com.andrewclam.toyblesensor.data.source.sensor.SensorRepositoryModule;
import com.andrewclam.toyblesensor.di.modules.ActivityBindingModule;
import com.andrewclam.toyblesensor.di.modules.ApplicationModule;
import com.andrewclam.toyblesensor.di.modules.SchedulerProviderModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * This is a Dagger component. Refer to {@link BleSensorApplication} for the list of Dagger
 * components used in this application.
 * <p>
 * Even though Dagger allows annotating a {@link Component} as a singleton, the code
 * itself must ensure only one instance of the class is created. This is done in
 * {@link BleSensorApplication}.
 * <p>
 * {@link AndroidSupportInjectionModule} is the module from Dagger.Android that
 * helps with the generation and location of sub-components.
 */
@Singleton
@Component(modules = {
    // data modules
    SensorRepositoryModule.class,
    AppDatabaseModule.class,

    // app wide modules
    ApplicationModule.class,
    ActivityBindingModule.class,
    SchedulerProviderModule.class,
    AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<BleSensorApplication> {

  // TasksRepository getTasksRepository();

  // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this)
  // .build().inject(this);
  // never having to instantiate any modules or say which module we are passing the application to.
  // Application will just be provided into our app graph now.
  @Component.Builder
  interface Builder {

    @BindsInstance
    AppComponent.Builder application(Application application);

    AppComponent build();
  }
}
