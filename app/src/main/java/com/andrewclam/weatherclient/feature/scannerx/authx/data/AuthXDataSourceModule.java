package com.andrewclam.weatherclient.feature.scannerx.authx.data;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import io.reactivex.annotations.NonNull;

/**
 * Dagger module for injecting a {@link AuthXDataSource}
 */
@Module
public abstract class AuthXDataSourceModule {
  @NonNull
  @Singleton
  @Binds
  abstract AuthXDataSource providesDataSource(AuthXCacheDataSource dataSource);
}
