package com.andrewclam.weatherclient.feature.scanner.service;

import com.andrewclam.weatherclient.di.ServiceScoped;

import javax.annotation.Nonnull;

import dagger.Binds;
import dagger.Module;

/**
 * Dagger module for injecting a {@link ScannerContract.Service}
 */
@Module
public abstract class ScannerServiceModule {
  @Nonnull
  @ServiceScoped
  @Binds
  abstract ScannerContract.Controller providesController(@Nonnull ScannerController scannerController);
}
