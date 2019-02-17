package com.andrewclam.weatherclient.feature.scanner.view;

import com.andrewclam.weatherclient.di.ServiceScoped;
import com.andrewclam.weatherclient.feature.scanner.service.ScannerService;

import javax.annotation.Nonnull;

import dagger.Binds;
import dagger.Module;

/**
 * Dagger module for injecting a {@link ScannerContract.View}
 */
@Module
public abstract class ScannerViewModule {
  @Nonnull
  @ServiceScoped
  @Binds
  abstract ScannerContract.Presenter providesPresenter(@Nonnull ScannerPresenter presenter);
}
