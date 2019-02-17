package com.andrewclam.weatherclient.feature.scanner.data.result;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import io.reactivex.annotations.NonNull;

/**
 * Dagger module for injecting a {@link ScannerResultDataSource}
 */
@Module
public abstract class ScannerResultDataSourceModule {
  @NonNull
  @Singleton
  @Binds
  abstract ScannerResultDataSource providesDataSource(ScannerResultCacheDataSource dataSource);
}
