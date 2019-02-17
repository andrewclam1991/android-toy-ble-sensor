package com.andrewclam.weatherclient.feature.scannerx.view;

import com.andrewclam.weatherclient.di.ActivityScoped;

import javax.annotation.Nonnull;

import dagger.Binds;
import dagger.Module;

/**
 * Dagger module for injecting a {@link ScannerXContract.View}
 */
@Module
public abstract class ScannerXViewModule {
  @Nonnull
  @ActivityScoped
  @Binds
  abstract ScannerXContract.Presenter providesPresenter(@Nonnull ScannerXPresenter presenter);
}
