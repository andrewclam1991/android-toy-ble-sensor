package com.andrewclam.weatherclient.feature.scanner.data.event;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import io.reactivex.annotations.NonNull;

/**
 * Dagger module for injecting a {@link ScannerEventDataSource}
 */
@Module
public abstract class ScannerEventDataSourceModule {
  @NonNull
  @Singleton
  @Binds
  abstract ScannerEventDataSource providesDataSource(ScannerEventCacheDataSource dataSource);
}
