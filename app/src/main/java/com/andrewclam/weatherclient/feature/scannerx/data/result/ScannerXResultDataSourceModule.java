package com.andrewclam.weatherclient.feature.scannerx.data.result;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import io.reactivex.annotations.NonNull;

/**
 * Dagger module for injecting a {@link ScannerXResultDataSource}
 */
@Module
public abstract class ScannerXResultDataSourceModule {
  @NonNull
  @Singleton
  @Binds
  abstract ScannerXResultDataSource providesDataSource(ScannerXResultCacheDataSource dataSource);
}
