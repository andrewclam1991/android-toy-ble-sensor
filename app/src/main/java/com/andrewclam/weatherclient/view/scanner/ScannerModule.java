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

package com.andrewclam.weatherclient.view.scanner;

import com.andrewclam.weatherclient.di.ActivityScoped;
import com.andrewclam.weatherclient.di.FragmentScoped;

import javax.annotation.Nonnull;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.reactivex.annotations.NonNull;

@Module
public abstract class ScannerModule {

  @FragmentScoped
  @ContributesAndroidInjector
  abstract ScannerFragment providesFragment();

  @NonNull
  @ActivityScoped
  @Binds
  abstract ScannerViewContract.Presenter providesPresenter(@Nonnull ScannerPresenter presenter);

  @NonNull
  @ActivityScoped
  @Binds
  abstract ScannerViewContract.ListPresenter providesListPresenter(@Nonnull ScannerPresenter presenter);
}
