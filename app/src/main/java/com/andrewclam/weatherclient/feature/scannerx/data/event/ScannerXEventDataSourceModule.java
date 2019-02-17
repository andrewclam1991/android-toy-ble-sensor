package com.andrewclam.weatherclient.feature.scannerx.data.event;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import io.reactivex.annotations.NonNull;

/**
 * Dagger module for injecting a {@link ScannerXEventDataSource}
 */
@Module
public abstract class ScannerXEventDataSourceModule {
  @NonNull
  @Singleton
  @Binds
  abstract ScannerXEventDataSource providesDataSource(ScannerXXEventCacheDataSource dataSource);
}
