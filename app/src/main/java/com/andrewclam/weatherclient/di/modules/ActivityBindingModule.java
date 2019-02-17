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

package com.andrewclam.weatherclient.di.modules;

import com.andrewclam.weatherclient.di.ActivityScoped;
import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.feature.scannerx.service.ScannerXServiceModule;
import com.andrewclam.weatherclient.feature.scannerx.service.ScannerXService;
import com.andrewclam.weatherclient.feature.scannerx.view.ScannerXView;
import com.andrewclam.weatherclient.feature.scannerx.view.ScannerXViewModule;
import com.andrewclam.weatherclient.service.scanner.ScannerAuthority;
import com.andrewclam.weatherclient.service.scanner.ScannerService;
import com.andrewclam.weatherclient.service.scanner.ScannerServiceModule;
import com.andrewclam.weatherclient.view.scanner.ScannerActivity;
import com.andrewclam.weatherclient.view.scanner.ScannerModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.reactivex.annotations.NonNull;

/**
 * We want Dagger.Android to create a Sub-component which has a parent Component of whichever
 * module ActivityBindingModule is on.
 * <p>
 * In our case that will be AppComponent. The beautiful part about this setup is that you never
 * need to tell AppComponent that it is going to have all these sub-components
 * nor do you need to tell these sub-components that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation @ActivityScoped
 * <p>
 * When Dagger.Android annotation processor runs it will create the sub-components for us.
 */
@Module
public abstract class ActivityBindingModule {
  @NonNull
  @ActivityScoped
  @ContributesAndroidInjector(modules = {ScannerModule.class})
  abstract ScannerActivity bindsScannerActivity();

  @NonNull
  @ServiceScoped
  @ContributesAndroidInjector(modules = {ScannerServiceModule.class})
  abstract ScannerService bindsScannerService();

  @NonNull
  @ServiceScoped
  @ContributesAndroidInjector(modules = ScannerXServiceModule.class)
  abstract ScannerXService scannerXService();

  @NonNull
  @ActivityScoped
  @ContributesAndroidInjector(modules = ScannerXViewModule.class)
  abstract ScannerXView scannerView();

  @NonNull
  @ActivityScoped
  @ContributesAndroidInjector
  abstract ScannerAuthority bindsScannerAuthority();
}
