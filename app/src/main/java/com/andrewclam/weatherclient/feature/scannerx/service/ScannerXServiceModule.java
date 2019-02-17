package com.andrewclam.weatherclient.feature.scannerx.service;

import com.andrewclam.weatherclient.di.ServiceScoped;

import javax.annotation.Nonnull;

import dagger.Binds;
import dagger.Module;

/**
 * Dagger module for injecting a {@link ScannerXContract.Service}
 */
@Module
public abstract class ScannerXServiceModule {
  @Nonnull
  @ServiceScoped
  @Binds
  abstract ScannerXContract.Controller providesController(@Nonnull ScannerXController scannerXController);
}
